package bean;

import org.springframework.security.core.userdetails.User;

public class LoginUserDetails extends User {
    private entity.User domainUser;

    public LoginUserDetails(entity.User domainUser) {
        super(domainUser.getName(), domainUser.getHashedPassword(), domainUser.getAuthorities());
        this.domainUser = domainUser;
    }
    
    public entity.User getDomainUser() {
        return this.domainUser;
    }
    
}
