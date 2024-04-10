package yourHOmeTEL.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.repository.UserRepository;

@Service
public class UserService implements GeneralService<UserE> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<UserE> findById(Long id) {
        return userRepository.findById(id);

    }

    @Override
    public UserE save(UserE user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(UserE user) {
        userRepository.delete(user);
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

    public Optional<UserE> findByNick(String nick) {
        return userRepository.findByNick(nick);
    }
    
    public Optional<UserE> findFirstByName(String name) {
        return userRepository.findFirstByName(name);
    }

    public Optional<UserE> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Page<UserE> findAll(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public Page<UserE> findByValidatedAndRejected(Boolean validated, Boolean rejected, Pageable pageable){
        return userRepository.findByValidatedAndRejected(validated, rejected, pageable);
    }

    public Page<UserE> findByRejected(Boolean validated, Pageable pageable){
        return userRepository.findByRejected(validated, pageable);
    }

    public Page<UserE> findByPhone(String phone,Pageable pageable){
        return userRepository.findByPhone(phone, pageable);
    }

    public Page<UserE> findLocationByName(String name, Pageable pageable){
        return userRepository.findLocationByName(name, pageable);
    }

    public Page<UserE> findByCollectionRolsContains(String rol, Pageable pageable){
        return userRepository.findByCollectionRolsContains(rol, pageable);
    }

    public List<UserE> findByValidatedAndRejected(Boolean validated, Boolean rejected){
        return userRepository.findByValidatedAndRejected(validated, rejected);
    }

    public List<UserE> findByRejected(Boolean validated){
        return userRepository.findByRejected(validated);
    }
    
    public List<UserE> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public List<UserE> findLocationByName(String name) {
        return userRepository.findLocationByName(name);
    }

    public List<UserE> findByHotelInReservations(@Param("hotel") Hotel hotel){
        return userRepository.findByHotelInReservations(hotel);
    }

    public List<UserE> findByCollectionRolsContains(String rol){
        return userRepository.findByCollectionRolsContains(rol);
    }

    public boolean existNick(String nick) {
        Optional<UserE> user = findByNick(nick);
        return user.isPresent();
    }

}
