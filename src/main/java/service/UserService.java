package service;

import entity.Permission;
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
                user.addPermission(new Permission("hoge"));
                break;
            case "user2":
                user = new User("2", name);
                user.addPermission(new Permission("hoge"));
                user.addPermission(new Permission("fuga"));
                break;
            case "admin":
                user = new User("9999", name);
                user.addPermission(new Permission("all"));
                break;
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
                result.addPermission(new Permission("hoge"));
                break;
            default:
                result.addPermission(new Permission("hoge"));
                result.addPermission(new Permission("fuga"));
        }

        return result;
    }
}
