package be.wouterversyck.shoppinglistapi;

import io.sentry.Sentry;
import io.sentry.spring.SentryExceptionResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableCaching
public class ApplicationConfiguration {

    private final String sentryDsn;

    public ApplicationConfiguration(@Value("${sentry.dsn:''}") final String sentryDsn) {
        this.sentryDsn = sentryDsn;
    }

    @Bean
    @Profile("prd")
    public HandlerExceptionResolver sentryExceptionResolver() {
        Sentry.init(sentryDsn);
        return new SentryExceptionResolver();
    }
}
