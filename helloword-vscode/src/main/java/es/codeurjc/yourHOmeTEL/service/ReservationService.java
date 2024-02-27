package es.codeurjc.yourHOmeTEL.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.repository.ReservationRepository;


@Service
public class ReservationService implements GeneralService<Reservation> {
    
    @Autowired
    private ReservationRepository repository;


    @Override
    public Optional <Reservation> findById(Long id){
        return repository.findById(id);

    }

    @Override
    public void save(Reservation go){
        
    }

    @Override
    public void delete(Reservation go){

    }

    @Override
    public List <Reservation> findAll() {
        return repository.findAll();
    }

    @Override
    public List <Reservation> findAll(Sort sort){
        if (!sort.equals(null)){
            return repository.findAll(sort);
        }
        else {
            return repository.findAll();
        }      
    }


    @Override
    public Boolean exist (Long id){
        return null;
    }

    public LocalDate toLocalDate(String date){
        String[] list;
        list = date.split("/");

        LocalDate localdate = LocalDate.of(Integer.parseInt(list[2]), Integer.parseInt(list[0]), Integer.parseInt(list[1]));
        return localdate;
    }
    
}
