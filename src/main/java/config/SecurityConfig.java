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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import service.LoginUserDetailsService;
import util.HttpServletUtil;

@EnableWebSecurity
@ComponentScan("service")
@Import(EntityManagerConfiguration.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REALM_NAME = "Auth Sample";
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
    
    @Autowired
    LoginUserDetailsService loginUserDetailsService;
    
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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(this.loginUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        LOGGER.debug("authenticationEntryPoint called");
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                LOGGER.debug("commence start");

                HttpServletUtil.addAccessControlHeader(response, AppConfiguration.CORS_ACCESS_CONTROL_ALLOW_ORIGIN, "true");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 
                // 認証情報がない場合
                if (!HttpServletUtil.hasAuthorization(request)) {
                    LOGGER.debug("no Authorization");
                    
                    if (HttpServletUtil.isXMLHttpRequest(request)) {
                        // XHR経由の場合は、ブラウザの認証ダイアログを抑制させるため
                        // www-Authenticate Header は追加しない
                        HttpServletUtil.setJsonBody(response, new ErrorResponse("no Authorization"));
                    } else {
                        // XHR経由でない場合は www-Authenticate Headerを追加する
                        HttpServletUtil.addWwwAuthenticateHeader(response, REALM_NAME);
                    }
                } else {
                    // 認証情報がある場合は、エラーを返す
                    LOGGER.debug("has Authorization (invalid user or password)");
                    HttpServletUtil.setJsonBody(response, new ErrorResponse("invalid user or password"));
                }
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