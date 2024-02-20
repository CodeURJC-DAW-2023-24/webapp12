package es.service;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;

public interface GeneralService<GenericObject> {

    public List <GenericObject> findById(Long id);
    public void save(GenericObject go);
    public void delete(GenericObject go);
    public List <GenericObject> findAll();
    public List <GenericObject> findAll(Sort sort);
    public Boolean exist (Long id);
}
