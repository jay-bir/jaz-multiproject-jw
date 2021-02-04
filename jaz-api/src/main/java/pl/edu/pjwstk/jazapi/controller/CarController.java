package pl.edu.pjwstk.jazapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jazapi.dto.CarDTO;
import pl.edu.pjwstk.jazapi.model.Car;
import pl.edu.pjwstk.jazapi.service.CarService;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cars")
public class CarController extends CrudController<Car, CarDTO> {

    public CarController(CarService service) {
        super(service);
    }

    @Override
    public Function<Car, CarDTO> transformToDTO() {
        return CarDTO::new;
    }

    @GetMapping("/manufacturer")
    public ResponseEntity<CollectionModel<EntityModel<CarDTO>>> getCarsWithSameManufacturer(@RequestParam String manufacturer){
        try {
            List<Car> cars = ((CarService) service).getByManufacturer(manufacturer);
            List<EntityModel<CarDTO>> carsWithLinks = cars.stream()
                    .map(car -> transformToDTO().apply(car))
                    .map(carDto -> addLinksForItem().apply(carDto))
                    .collect(Collectors.toList());

            var payload = addLinksForCollection().apply(carsWithLinks);
            return new ResponseEntity<>(payload, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Function<Collection<EntityModel<CarDTO>>, CollectionModel<EntityModel<CarDTO>>> addLinksForCollection() {
        return cars -> CollectionModel.of(cars,
                linkTo(methodOn(CarController.class).getAll()).withRel("cars"));
    }

    @Override
    public Function<CarDTO, EntityModel<CarDTO>> addLinksForItem() {
        return car -> EntityModel.of(car,
                linkTo(methodOn(CarController.class).getById(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).getCarsWithSameManufacturer(car.getManufacturer())).withRel("same_manufacturer"),
                linkTo(methodOn(CarController.class).getAll()).withRel("cars"));
    }
}
