package es.codeurjc.yourHOmeTEL.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;


@Service
public class HotelService implements GeneralService<Hotel> {
    
    @Autowired
    private HotelRepository repository;


    @Override
    public Optional <Hotel> findById(Long id){
        return repository.findById(id);

    }

    @Override
    public void save(Hotel go){
        
    }

    @Override
    public void delete(Hotel go){

    }

    @Override
    public List <Hotel> findAll() {
        return repository.findAll();
    }

    @Override
    public List <Hotel> findAll(Sort sort){
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

    public Room checkRooms(Long id, LocalDate checkIn, LocalDate checkOut) {
        Hotel hotel = this.findById(id).orElseThrow();
        Integer i = 0;
        List <Room> rooms = hotel.getRooms();
        Boolean roomLocated = false;
        Room room = null;
        while ((i < hotel.getnumRooms()) && !(roomLocated)){
            if (rooms.get(i).available(checkIn, checkOut)){
                room = rooms.get(i);
                roomLocated = true;
            }
            else
                i++;
        }
        return room;
    }
    
}
