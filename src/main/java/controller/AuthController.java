package controller;

import entity.User;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.UserNotFoundException;
import service.UserService;

@RestController
public class AuthController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UserService service;
    
    @RequestMapping(method = RequestMethod.GET, value = "/api")
    public User getAuthentication(Principal principal) throws UserNotFoundException {
        LOGGER.debug(String.format("loggin user id: %s", principal.getName()));
        return service.getByName(principal.getName());
    }
}
