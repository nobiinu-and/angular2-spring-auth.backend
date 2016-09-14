package controller;

import entity.User;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.UserNotFoundException;
import service.UserService;

@RestController
public class UserController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService service;
    
    @RequestMapping(method = RequestMethod.GET, value = "/api/users")
    public List<User> list(Principal principal) throws UserNotFoundException {
        LOGGER.debug(String.format("loggin user id: %s", principal.getName()));
        List<User> list = new ArrayList<>();
        list.add(service.getByName("user1"));
        list.add(service.getByName("user2"));
        list.add(service.getByName("admin"));
        return list;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/api/users/{id}")
    public User get(@PathVariable("id") String id, Principal principal) throws UserNotFoundException {
        LOGGER.debug(String.format("loggin user id: %s", principal.getName()));
        return service.get(id);
    }
    
}
