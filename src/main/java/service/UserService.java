package service;

import entity.User;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UserRepository;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;
    
    public List<User> findAll() {
        return this.userRepository.findAll();
    }
    
    public User findOneByName(String name) throws UserNotFoundException {
        LOGGER.debug("findOneByName name:" + name);
        
        User user = null;
        try {
            user = this.userRepository.findOneByName(name);
        } catch (Exception ex) {
            LOGGER.error("findOne userRepository.findOne failed", ex);
        }

        if (user == null) {
            LOGGER.debug("findOneByName not found. name:" + name);
            throw new UserNotFoundException("id: " + name + " not found.");
        }

        LOGGER.debug("findOneByName found. id:" + user.getId() + " name:" + user.getName());
        
        return user;
    }
    
    public User findOne(String id) throws UserNotFoundException {
        
        LOGGER.debug("findOne id:" + id);
        
        switch(id) {
            case "9":
                throw new IllegalArgumentException("hoge");
            case "99":
                throw new RuntimeException("fuga");
            case "10":
                throw new UserNotFoundException(String.format("id '%s' is not found", id));
        }
        
        User user = null;
        try {
            user = this.userRepository.findOne(id);
        } catch (Exception ex) {
            LOGGER.error("findOne userRepository.findOne failed", ex);
        }

        if (user == null) {
            LOGGER.debug("findOne not found. id:" + id);
            throw new UserNotFoundException("id: " + id + " not found.");
        }

        LOGGER.debug("findOne found. id:" + user.getId() + " name:" + user.getName());

        return user;
    }
}
