package es.codeurjc.yourHOmeTEL.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;


@Service
public class HotelService implements GeneralService<Hotel> {

    @Autowired
    HotelRepository hotelRepository;

    @Override
    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }

    @Override
    public void save(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Override
    public void delete(Hotel hotel) {
        hotelRepository.delete(hotel);;
    }

    @Override
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    @Override
    public List<Hotel> findAll(Sort sort) {
        if (!sort.equals(null)){
            return hotelRepository.findAll(sort);
        }
        else {
            return hotelRepository.findAll();
        }  
    }

    @Override
    public Boolean exist(Long id) {
        Optional <Hotel> user = hotelRepository.findById(id);
        if (user.isPresent())
            return true;
        else
            return false;
    }

    public List<UserE> getValidClients(Hotel hotel){
        LocalDate today = LocalDate.now();
        List <UserE> validClients = new ArrayList<>();
        List <Reservation> hotelReservations = hotel.getReservations();

        for (Reservation reservation : hotelReservations){
			if (reservation.getCheckOut().isAfter(today)){
                UserE user = reservation.getUser();
                if (!validClients.contains(user))
                    validClients.add(user);
		    }
        }
        return validClients;
    }

    public Room checkRooms(Long id, LocalDate checkIn, LocalDate checkOut, Integer numPeople) {
        Hotel hotel = this.findById(id).orElseThrow();
        Integer i = 0;
        List <Room> rooms = hotel.getRooms();
        Boolean roomLocated = false;
        Room room = null;
        while ((i < hotel.getNumRooms()) && !(roomLocated)){
            if(rooms.get(i).getMaxClients() == numPeople){
                if (rooms.get(i).available(checkIn, checkOut)){
                    room = rooms.get(i);
                    roomLocated = true;
                }
                else
                    i++;
            }
            else
                i++;
        }
        return room;
    }
    
}

