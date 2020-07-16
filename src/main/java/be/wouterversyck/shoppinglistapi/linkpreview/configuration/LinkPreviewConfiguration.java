package be.wouterversyck.shoppinglistapi.linkpreview.configuration;

import be.wouterversyck.shoppinglistapi.linkpreview.services.LinkPreviewService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LinkPreviewConfiguration {

    @Value("${link-preview.token}")
    private String token;

    @Bean
    public LinkPreviewService linkPreviewService() {
        return new LinkPreviewService(token, new RestTemplate());
    }
}
