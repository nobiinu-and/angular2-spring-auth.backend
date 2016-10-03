package config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"controller", "service", "repository", "entity"})
public class AppConfiguration {
    
    public static final String CORS_ACCESS_CONTROL_ALLOW_ORIGIN = "http://localhost:5555";
    
}
