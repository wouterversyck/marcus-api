package be.wouterversyck.shoppinglistapi.debug.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mdc")
public class MDCProperties {
    private String correlationIdHeaderKey;
    private String userMdcKey;
    private String correlationIdMdcKey;
}
