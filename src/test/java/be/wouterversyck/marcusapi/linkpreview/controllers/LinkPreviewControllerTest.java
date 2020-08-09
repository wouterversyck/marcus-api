package be.wouterversyck.marcusapi.linkpreview.controllers;

import be.wouterversyck.marcusapi.linkpreview.models.LinkPreview;
import be.wouterversyck.marcusapi.linkpreview.services.LinkPreviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkPreviewControllerTest {

    @Mock
    private LinkPreviewService linkPreviewService;

    @InjectMocks
    private LinkPreviewController linkPreviewController;

    @Test
    void shouldDelegateLinkPreviewRequest() {
        when(linkPreviewService.getLinkPreview("LINK")).thenReturn(
                LinkPreview.builder()
                    .title("TITLE").build()
        );

        var result = linkPreviewController.getLinkPreview("LINK");

        assertThat(result.getTitle()).isEqualTo("TITLE");
    }

}