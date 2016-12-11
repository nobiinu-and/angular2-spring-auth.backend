package controller;

import bean.LoginUserDetails;
import entity.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    public User getAuthentication(@AuthenticationPrincipal LoginUserDetails user) throws UserNotFoundException {
        LOGGER.debug(String.format("loggin user id: %s", user.getUsername()));
        return service.findOneByName(user.getUsername());
    }

    @RequestMapping(method = RequestMethod.GET, value="/api/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            LOGGER.debug("logout: auth is null");
            return "{}";
        }
        SecurityContextLogoutHandler scHandler = new SecurityContextLogoutHandler();
        scHandler.logout(request, response, auth);
        LOGGER.debug("logout: done");
        return "{}";
    }
    
}
