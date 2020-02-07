package be.wouterversyck.shoppinglistapi.security.config;

import be.wouterversyck.shoppinglistapi.security.filters.JwtLoginFilter;
import be.wouterversyck.shoppinglistapi.security.filters.JwtAuthenticationFilter;
import be.wouterversyck.shoppinglistapi.security.handlers.JwtAuthenticationFailureHandler;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import be.wouterversyck.shoppinglistapi.security.services.JwtService;
import be.wouterversyck.shoppinglistapi.security.services.SecurityUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final String jwtSecretKey;

    public JwtSecurityConfiguration(final UserService userService, @Value("${JWT_SECRET}") final String jwtSecretKey) {
        this.userService = userService;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/public/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getJwtLoginFilter())
                .addFilterAfter(getAuthorizationFilter(), JwtLoginFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public UsernamePasswordAuthenticationFilter getJwtLoginFilter() throws Exception {
        return new JwtLoginFilter(authenticationManager(), getJwtService(), getFailureHandler());
    }

    @Bean
    public OncePerRequestFilter getAuthorizationFilter() {
        return new JwtAuthenticationFilter(getJwtService());
    }

    @Bean
    public AuthenticationFailureHandler getFailureHandler(){
        return new JwtAuthenticationFailureHandler();
    }

    @Bean
    public JwtService getJwtService() {
        return new JwtService(jwtSecretKey);
    }

    /*
        Will be picked up by spring boot security auto config
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
        Will be picked up by spring boot security auto config
     */
    @Bean
    public UserDetailsService getSecurityUserService() {
        return new SecurityUserService(userService);
    }
}
