package be.wouterversyck.shoppinglistapi;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@EnableCaching
public class ApplicationConfiguration {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        final var filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setIncludeClientInfo(true);
        filter.setIncludeHeaders(true);
        filter.setHeaderPredicate(e -> !e.equalsIgnoreCase("authorization"));
        return filter;
    }

}
