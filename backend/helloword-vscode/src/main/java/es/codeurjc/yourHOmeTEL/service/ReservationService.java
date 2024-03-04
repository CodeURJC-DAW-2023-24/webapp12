package es.codeurjc.yourHOmeTEL.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.ReservationRepository;

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
    public LocalDate toLocalDate(String date) {
        String[] list;
        list = date.split("/");

        LocalDate localdate = LocalDate.of(Integer.parseInt(list[2]), Integer.parseInt(list[0]),
                Integer.parseInt(list[1]));
        return localdate;
    }

}
