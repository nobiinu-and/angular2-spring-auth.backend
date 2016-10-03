package service;

import entity.Authority;
import entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getByName(String name) throws UserNotFoundException {
        if (name == null) {
            throw new UserNotFoundException("name is null");
        }
        
        User user = null;
        switch (name) {
            case "user1":
                user = new User("1", name);
                user.addAuthorities(new Authority("ROLE_USER"));
                user.setPassword("u");
                break;
            case "user2":
                user = new User("2", name);
                user.addAuthorities(new Authority("ROLE_USER"));
                user.setPassword("u");
                break;
            case "admin":
                user = new User("9999", name);
                user.addAuthorities(new Authority("ROLE_ADMIN"));
                user.setPassword("u");
                break;
        }
        
        if (user == null) {
            throw new UserNotFoundException("user does not exists");
        }
        
        return user;
    }
    
    public User get(String id) throws UserNotFoundException {
        if (id == null) {
            throw new UserNotFoundException("id is null");
        }
        
        switch(id) {
            case "9":
                throw new IllegalArgumentException("hoge");
            case "99":
                throw new RuntimeException("fuga");
            case "10":
                throw new UserNotFoundException(String.format("id '%s' is not found", id));
        }
        
        User result = new User(id, "name-" + id);
        
        switch(id) {
            case "1":
                result.addAuthorities(new Authority("hoge"));
                break;
            default:
                result.addAuthorities(new Authority("hoge"));
                result.addAuthorities(new Authority("fuga"));
        }

        return result;
    }
}
