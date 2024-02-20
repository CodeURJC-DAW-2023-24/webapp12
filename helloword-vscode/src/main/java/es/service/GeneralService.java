package es.service;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;

public interface GeneralService<GenericObject> {

    public Optional <GenericObject> findById(Long id);
    public void save(GenericObject go);
    public void delete(GenericObject go);
    public Optional  <GenericObject> findAll();
    public Optional <GenericObject> findAll(Sort sort);
    public Boolean exist (Long id);
}
