package yourHOmeTEL.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.repository.RoomRepository;

@Service
public class RoomService implements GeneralService<Room> {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);

    }

    public Page<Room> findByMaxClients(int maxClients, Pageable pageable){
        return roomRepository.findByMaxClients(maxClients, pageable);
    }

    public Page<Room> findByHotel_Id(Long hotelId, Pageable pageable){
        return roomRepository.findByHotel_Id(hotelId, pageable);
    }

    public Room findByHotel_Id(Long hotelId ){
        return roomRepository.findByHotel_Id(hotelId);
    }

    public List<Room> findByMaxClients(int maxClients){
        return roomRepository.findByMaxClients(maxClients);
    }

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void delete(Room room) {
        roomRepository.delete(room);
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> findAll(Sort sort) {
        if (!sort.equals(null)) {
            return roomRepository.findAll(sort);
        } else {
            return roomRepository.findAll();
        }
    }

    @Override
    public Boolean exist(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent())
            return true;
        else
            return false;
    }
}
