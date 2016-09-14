package config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer extends AbstractSecurityWebApplicationInitializer {
    private static Logger logger = LoggerFactory.getLogger(WebAppInitializer.class);
    
    public WebAppInitializer() {
        super(SecurityConfig.class);
    }
    
    @Override
    public void afterSpringSecurityFilterChain(ServletContext container) {
        logger.debug("Initialize Start");
        AnnotationConfigWebApplicationContext dispatcherContext =
                        new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(AppConfiguration.class);

        container.addFilter("cors", new CORSFilter())
            .addMappingForUrlPatterns(null, true, "/*");
        
        ServletRegistration.Dynamic dispatcher =
                        container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
