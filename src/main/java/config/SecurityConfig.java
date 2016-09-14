package config;

import bean.ErrorResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import util.HttpServletUtil;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REALM_NAME = "Auth Sample";
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        LOGGER.debug("configureGlobal(AuthenticationManagerBuilder) start");
        auth.inMemoryAuthentication()
                .withUser("user1").password("u").roles("USER")
                .and()
                .withUser("user2").password("u").roles("USER")
                .and()
                .withUser("admin").password("a").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.debug("configure(HttpSecurity) start");
        http
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // preflightのためにすべてのPATHでOPTIONSを有効にする
            .antMatchers("/api/users").hasRole("ADMIN")
            .antMatchers("/api/**").fullyAuthenticated()
            .anyRequest().permitAll()
            .and()
            .httpBasic()
                .authenticationEntryPoint(this.authenticationEntryPoint()) // Basic認証を使う場合エラーハンドリングはこちらで行う必要がある
            .and()
            .exceptionHandling()
                .accessDeniedHandler(this.accessDeninedHandler())
            .and()
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            ;
    }    
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        LOGGER.debug("authenticationEntryPoint called");
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                LOGGER.debug("commence start");

                HttpServletUtil.addAccessControlHeader(response, AppConfiguration.CORS_ACCESS_CONTROL_ALLOW_ORIGIN, "true");
 
                // 認証情報がない場合
                if (!HttpServletUtil.hasAuthorization(request)) {
                    LOGGER.debug("no Authorization");
                    
                    // XHR経由でない場合は www-Authenticate Headerを追加する
                    if (!HttpServletUtil.isXMLHttpRequest(request)) {
                        HttpServletUtil.addWwwAuthenticateHeader(response, REALM_NAME);
                    }
                    
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "");
                    return;
                }
                
                // 認証情報がある場合は、エラーを返す
                LOGGER.debug("has Authorization (invalid user or password)");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                HttpServletUtil.setJsonBody(response, new ErrorResponse("invalid user or password"));
            }
        };
    }

    @Bean
    public AccessDeniedHandler accessDeninedHandler() {
        LOGGER.debug("accessDeninedHandler called");
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException deniedException) throws IOException, ServletException {
                LOGGER.debug("handle start");

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                LOGGER.debug(String.format("username: %s", auth.getName()));
                LOGGER.debug(String.format("auth: %s", auth.toString()));

                HttpServletUtil.addAccessControlHeader(response, AppConfiguration.CORS_ACCESS_CONTROL_ALLOW_ORIGIN, "true");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                HttpServletUtil.setJsonBody(response, new ErrorResponse("no permission"));
            }
        };
    }
    
}