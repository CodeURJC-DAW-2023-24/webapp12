package es.codeurjc.yourHOmeTEL.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.repository.RoomRepository;

@Service
public class RoomService implements GeneralService<Room> {

    @Autowired
    private RoomRepository repository;

    @Override
    public Optional<Room> findById(Long id) {
        return repository.findById(id);

    }

    @Override
    public void save(Room go) {

    }

    @Override
    public void delete(Room go) {

    }

    @Override
    public List<Room> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Room> findAll(Sort sort) {
        if (!sort.equals(null)) {
            return repository.findAll(sort);
        } else {
            return repository.findAll();
        }
    }

    @Override
    public Boolean exist(Long id) {
        return null;
    }
}
