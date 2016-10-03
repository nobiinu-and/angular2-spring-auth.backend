package service;

import bean.LoginUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserDetailsService.class);

    @Autowired
    private UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        LOGGER.debug("loadUserByUsername name:" + name);
        try {
            entity.User domainUser = userService.findOneByName(name);
            return new LoginUserDetails(domainUser);
        } catch( UserNotFoundException ex ) {
            throw new UsernameNotFoundException("ユーザは存在しません", ex);
        }
    }
    
}
