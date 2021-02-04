package pl.edu.pjwstk.jazapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jazapi.dto.AddOnDTO;
import pl.edu.pjwstk.jazapi.model.AddOn;
import pl.edu.pjwstk.jazapi.service.AddOnService;

import java.util.Collection;
import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/addons")
public class AddOnController extends CrudController<AddOn, AddOnDTO> {

    public AddOnController(AddOnService service) {
        super(service);
    }

    @Override
    public Function<AddOn, AddOnDTO> transformToDTO() {
        return AddOnDTO::new;
    }

    @Override
    public Function<AddOnDTO, EntityModel<AddOnDTO>> addLinksForItem() {
        return addOnDTO -> EntityModel.of(addOnDTO,
                linkTo(methodOn(AddOnController.class).getAll()).withRel("addons"),
                linkTo(methodOn(AddOnController.class).getById(addOnDTO.getId())).withSelfRel());
    }

    @Override
    public Function<Collection<EntityModel<AddOnDTO>>, CollectionModel<EntityModel<AddOnDTO>>> addLinksForCollection() {
        return addOns -> CollectionModel.of(addOns,
                linkTo(methodOn(AddOnController.class).getAll()).withRel("addons"));
    }
}

