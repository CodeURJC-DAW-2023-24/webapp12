package yourHOmeTEL.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.repository.ReservationRepository;

@Service
public class ReservationService implements GeneralService<Reservation> {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public Page<Reservation> findByUser_Name(String name, Pageable pageable){
        return reservationRepository.findByUser_Name(name, pageable);
    }

    public Page<Reservation> findByHotel_Name(String name, Pageable pageable){
        return reservationRepository.findByHotel_Name(name, pageable);
    }

    public Page<Reservation> findByUser_Id(Long userId, Pageable pageable){
        return reservationRepository.findByUser_Id(userId, pageable);
    }

    public Page<Reservation> findByHotel_Id(Long hotelId, Pageable pageable){
        return reservationRepository.findByHotel_Id(hotelId, pageable);
    }

    public Page<Reservation> findByRoom_Id(Long roomId, Pageable pageable){
        return reservationRepository.findByRoom_Id(roomId, pageable);
    }

    public List<Reservation> findByUser_Name(String name){
        return reservationRepository.findByUser_Name(name);
    }

    public List<Reservation> findByHotel_Name(String name){
        return reservationRepository.findByHotel_Name(name);
    } 

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> findAll(Sort sort) {
        if (!sort.equals(null)) {
            return reservationRepository.findAll(sort);
        } else {
            return reservationRepository.findAll();
        }
    }

    @Override
    public Boolean exist(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent())
            return true;
        else
            return false;
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public LocalDate toLocalDate(String date) {
        String[] list;
        list = date.split("/");

        LocalDate localdate = LocalDate.of(Integer.parseInt(list[2]), Integer.parseInt(list[0]),
                Integer.parseInt(list[1]));
        return localdate;
    }

}
