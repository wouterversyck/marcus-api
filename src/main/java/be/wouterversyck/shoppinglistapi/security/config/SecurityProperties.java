package be.wouterversyck.shoppinglistapi.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class SecurityProperties {

    private String authLoginUrl;

    private String secretKey;

    // JWT token defaults
    private String tokenHeader;
    private String responseTokenHeader;
    private String tokenPrefix;
    private String tokenType;
    private String tokenIssuer;
    private String tokenAudience;
    private long expiration;
    private String googleClientId;
}
