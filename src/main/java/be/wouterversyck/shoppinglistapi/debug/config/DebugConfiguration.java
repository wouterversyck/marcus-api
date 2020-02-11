package be.wouterversyck.shoppinglistapi.debug.config;

import be.wouterversyck.shoppinglistapi.debug.filters.MDCFilter;
import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MDCProperties.class)
public class DebugConfiguration {

    @Bean
    public FilterRegistrationBean<MDCFilter> servletRegistrationBean(final SecurityProperties securityProperties,
                                                                     final JwtService jwtService,
                                                                     final MDCProperties mdcProperties) {
        final FilterRegistrationBean<MDCFilter> registrationBean = new FilterRegistrationBean<>();
        final MDCFilter logbackMDCFilter = new MDCFilter(jwtService, securityProperties, mdcProperties);
        registrationBean.setFilter(logbackMDCFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
