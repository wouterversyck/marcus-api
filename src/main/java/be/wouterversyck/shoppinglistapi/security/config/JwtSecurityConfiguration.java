package be.wouterversyck.shoppinglistapi.security.config;

import be.wouterversyck.shoppinglistapi.security.filters.JwtLoginFilter;
import be.wouterversyck.shoppinglistapi.security.filters.JwtAuthenticationFilter;
import be.wouterversyck.shoppinglistapi.security.handlers.JwtAuthenticationFailureHandler;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final SecurityProperties properties;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/public/*", "/oauth/*").permitAll()
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
        return new JwtService(properties);
    }

    /*
        Will be picked up by spring boot security auto config
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Google oauth stuff
     */
    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        final HttpTransport httpTransport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        return new GoogleIdTokenVerifier.Builder(httpTransport, jacksonFactory)
                .setAudience(Collections.singletonList(properties.getGoogleClientId()))
                .setIssuer("accounts.google.com")
                .build();

    }
}
