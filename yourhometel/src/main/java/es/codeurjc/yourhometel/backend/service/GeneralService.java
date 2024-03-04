package es.codeurjc.yourhometel.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

public interface GeneralService<GenericObject> {

    public Optional<GenericObject> findById(Long id);

    public void save(GenericObject go);

    public void delete(GenericObject go);

    public List<GenericObject> findAll();

    public List<GenericObject> findAll(Sort sort);

    public Boolean exist(Long id);
}
