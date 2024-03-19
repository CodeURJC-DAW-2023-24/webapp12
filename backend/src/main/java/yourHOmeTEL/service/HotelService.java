package yourHOmeTEL.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.repository.HotelRepository;

@Service
public class HotelService implements GeneralService<Hotel> {

    @Autowired
    HotelRepository hotelRepository;

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);

    @Override
    public void save(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Override
    public void delete(Hotel hotel) {
        hotelRepository.delete(hotel);
        ;
    }
    
    @Override
    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }

    public List<Hotel> findByName(String name){
        return hotelRepository.findByName(name);
    }

    public List<Hotel> findByLocation(String location){
        return hotelRepository.findByLocation(location);
    }

    public Page<Hotel> findByManager_Id(Long managerId, Pageable pageable) {
        log.info("Manager ID: {}", managerId);
    
        Page<Hotel> hotels = hotelRepository.findByManager_Id(managerId, pageable);
        log.info("Hotels: {}", hotels);
    
        return hotels;
    }

    public Page<Hotel> findTop6ByManager_Validated(Boolean validated, Pageable pageable){
        return hotelRepository.findTop6ByManager_Validated(validated, pageable);
    }

    public Page<Hotel> findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(Boolean validated,
            String searchValue, Pageable pageable){
                return hotelRepository.findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(validated, searchValue, pageable);
            }

    public List<Hotel> findTop6ByManager_Validated(Boolean validated){
        return hotelRepository.findTop6ByManager_Validated(validated);
    }

    public List<Hotel> findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(Boolean validated,
            String searchValue){
                return hotelRepository.findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(validated, searchValue);
            }

    @Override
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    @Override
    public List<Hotel> findAll(Sort sort) {
        if (!sort.equals(null)) {
            return hotelRepository.findAll(sort);
        } else {
            return hotelRepository.findAll();
        }
    }

    @Override
    public Boolean exist(Long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if (hotel.isPresent())
            return true;
        else
            return false;
    }

    public List<UserE> getValidClients(Hotel hotel) {
        LocalDate today = LocalDate.now();
        List<UserE> validClients = new ArrayList<>();
        List<Reservation> hotelReservations = hotel.getReservations();

        for (Reservation reservation : hotelReservations) {
            if (reservation.getCheckOut().isAfter(today)) {
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
        List<Room> rooms = hotel.getRooms();
        Boolean roomLocated = false;
        Room room = null;
        while ((i < hotel.getNumRooms()) && !(roomLocated)) {
            if (rooms.get(i).getMaxClients() == numPeople) {
                if (rooms.get(i).available(checkIn, checkOut)) {
                    room = rooms.get(i);
                    roomLocated = true;
                } else
                    i++;
            } else
                i++;
        }
        return room;
    }

    public void deleteById(Long id){
        hotelRepository.deleteById(id);
    }

    public long count(){
        return hotelRepository.count();
    }

}
