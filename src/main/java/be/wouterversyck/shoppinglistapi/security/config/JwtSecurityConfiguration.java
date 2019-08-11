package be.wouterversyck.shoppinglistapi.security.config;

import be.wouterversyck.shoppinglistapi.security.filters.JwtAuthenticationFilter;
import be.wouterversyck.shoppinglistapi.security.filters.JwtAuthorizationFilter;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import be.wouterversyck.shoppinglistapi.security.services.JwtService;
import be.wouterversyck.shoppinglistapi.security.services.SecurityUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserService userService;

    public JwtSecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/public").permitAll()
                .antMatchers("/h2-console").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), getJwtService()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), getJwtService()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /*
        Will be picked up by spring boot security auto config
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }

    @Bean
    public JwtService getJwtService() {
        return new JwtService();
    }

    /*
        Will be picked up by spring boot security auto config
     */
    @Bean
    public UserDetailsService getSecurityUserService() {
        return new SecurityUserService(userService);
    }
}
