package be.wouterversyck.marcusapi.linkpreview.services;

import be.wouterversyck.marcusapi.linkpreview.models.LinkPreview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LinkPreviewServiceTest {
    private static final String TOKEN = "TOKEN";
    private static final String URL = "https://api.linkpreview.net/?key=%s&q=";

    @Mock
    private RestTemplate restTemplate;

    private LinkPreviewService linkPreviewService;

    @BeforeEach
    public void setup() {
        linkPreviewService = new LinkPreviewService(TOKEN, restTemplate);
    }

    @Test
    void shouldDelegateToRestTemplateWithToken() {
        var url = format(URL, TOKEN);
        when(restTemplate.getForObject(url + "LINK", LinkPreview.class)).thenReturn(
                LinkPreview.builder()
                        .title("TITLE")
                        .build()
        );

        var linkPreview = linkPreviewService.getLinkPreview("LINK");

        assertThat(linkPreview.getTitle()).isEqualTo("TITLE");
    }
}