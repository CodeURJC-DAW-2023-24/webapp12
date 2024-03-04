package es.codeurjc.yourHOmeTEL.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;

@Service
public class UserService implements GeneralService<UserE> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public Optional<UserE> findById(Long id) {
        return userRepository.findById(id);

    }

    @Override
    public void save(UserE go) {

    }

    @Override
    public void delete(UserE go) {

    }

    @Override
    public List<UserE> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserE> findAll(Sort sort) {
        if (!sort.equals(null)) {
            return userRepository.findAll(sort);
        } else {
            return userRepository.findAll();
        }
    }

    @Override
    public Boolean exist(Long id) {
        Optional<UserE> user = userRepository.findById(id);
        if (user.isPresent())
            return true;
        else
            return false;
    }

    public Optional<UserE> findFirstByName(String name) {
        return userRepository.findFirstByName(name);
    }

    public Optional<UserE> findByName(String name) {
        return userRepository.findByName(name);
    }

    public List<UserE> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public List<UserE> findLocationByName(String name) {
        return userRepository.findLocationByName(name);
    }

    public Optional<UserE> findByNick(String nick) {
        return userRepository.findByNick(nick);
    }

    public boolean existNick(String nick) {
        Optional<UserE> user = findByNick(nick);
        return user.isPresent();
    }

    public List<Hotel> findRecomendedHotels(int numHotels, List<Reservation> userReservations, UserE targetUser) {
        List<UserE> recomendedUsers = new ArrayList<>();
        List<Hotel> recomendedHotels = new ArrayList<>();

        for (Reservation reservation : userReservations) {
            Hotel reservedHotel = reservation.getHotel();
            recomendedUsers = userRepository.findByHotelInReservations(reservedHotel);
            if (recomendedUsers.contains(targetUser)) // removes self from recommendations
                recomendedUsers.remove(targetUser);
            for (UserE recommendedUser : recomendedUsers) {
                for (Reservation recommendedUserReservation : recommendedUser.getReservations()) {
                    Hotel recommendedHotel = recommendedUserReservation.getHotel();
                    Boolean validHotel = recommendedHotel.getManager().getvalidated();

                    if ((!recomendedHotels.contains(recommendedHotel)) && validHotel) {
                        recomendedHotels.add(recommendedHotel);
                        if (recomendedHotels.size() == (numHotels))
                            return recomendedHotels;
                    }
                }
            }
        }
        return recomendedHotels;
    }

}
