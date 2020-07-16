package be.wouterversyck.shoppinglistapi.linkpreview.services;

import be.wouterversyck.shoppinglistapi.linkpreview.models.LinkPreview;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

public class LinkPreviewService {
    private final String apiUrl;
    private final RestTemplate restTemplate;

    public LinkPreviewService(final String token, final RestTemplate restTemplate) {
        this.apiUrl = format("https://api.linkpreview.net/?key=%s&q=", token);
        this.restTemplate = restTemplate;
    }

    public LinkPreview getLinkPreview(final String link) {
        return restTemplate.getForObject(apiUrl + link, LinkPreview.class);
    }
}
