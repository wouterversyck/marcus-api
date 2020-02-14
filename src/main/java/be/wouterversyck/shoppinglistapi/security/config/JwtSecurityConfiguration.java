package be.wouterversyck.shoppinglistapi.security.config;

import be.wouterversyck.shoppinglistapi.security.filters.JwtLoginFilter;
import be.wouterversyck.shoppinglistapi.security.filters.JwtAuthenticationFilter;
import be.wouterversyck.shoppinglistapi.security.handlers.JwtAuthenticationFailureHandler;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import be.wouterversyck.shoppinglistapi.security.services.SecurityUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SecurityProperties.class)
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final String jwtSecretKey;
    private final SecurityProperties properties;

    public JwtSecurityConfiguration(final UserService userService,
                                    @Value("${JWT_SECRET}") final String jwtSecretKey,
                                    final SecurityProperties properties) {
        this.userService = userService;
        this.jwtSecretKey = jwtSecretKey;
        this.properties = properties;
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
                .addFilterAfter(getAuthenticationFilter(), JwtLoginFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public UsernamePasswordAuthenticationFilter getJwtLoginFilter() throws Exception {
        return new JwtLoginFilter(authenticationManager(), getJwtService(), getFailureHandler(), properties);
    }

    @Bean
    public OncePerRequestFilter getAuthenticationFilter() {
        return new JwtAuthenticationFilter(getJwtService(), properties);
    }

    @Bean
    public AuthenticationFailureHandler getFailureHandler(){
        return new JwtAuthenticationFailureHandler();
    }

    @Bean
    public JwtService getJwtService() {
        return new JwtService(jwtSecretKey, properties);
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
