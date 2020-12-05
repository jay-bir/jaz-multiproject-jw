package pl.edu.pjwstk.jazapi.service;

import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pjwstk.jazapi.exception.WrongSortParamsException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CrudService<T extends DbEntity> {
    JpaRepository<T, Long> repository;

    public CrudService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public List<T> getAll(int page, int size, List<String> sortParams) {
        Sort.Direction d;
        try {
            d = Sort.Direction.fromString(sortParams.remove(0));
        }
        catch(IllegalArgumentException e){
            throw new WrongSortParamsException("First sort param should be direction, next should be object properties");
        }
        String[] properties = sortParams.toArray(String[]::new);

        Iterable<T> items = repository.findAll(PageRequest.of(page,size, Sort.by(d,properties)));
        var itemList = new ArrayList<T>();

        items.forEach(itemList::add);

        return itemList;
    }

    public T getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        Optional<T> item = repository.findById(id);

        if (item.isPresent()) {
            repository.delete(item.orElseThrow());
        }
    }

    public abstract T createOrUpdate(T updateEntity);
}
