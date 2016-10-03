package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class User {
    private String id;
    private String name;
    private String hashedPassword;
    private List<Authority> authorities = new ArrayList<>();

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @JsonIgnore
    public String getHashedPassword() {
        return this.hashedPassword;
    }
    
    public void setHashedPassword(String value) {
        this.hashedPassword = value;
    }
    
    public void setPassword(String rawPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        this.hashedPassword = encoder.encode(rawPassword);
    }
    
    @JsonIgnore
    public List<Authority> getAuthorities() {
        return this.authorities;
    }

    public void addAuthorities(Authority value) {
        this.authorities.add(value);
    }    
}
