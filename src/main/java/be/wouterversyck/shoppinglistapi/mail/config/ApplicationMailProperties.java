package be.wouterversyck.shoppinglistapi.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mail")
public class ApplicationMailProperties {
    private String smtpUsername;
    private String smtpPassword;
    private String smtpHost;
    private int smtpPort;
    private boolean smtpStartTtls;
    private boolean smtpAuth;
}
