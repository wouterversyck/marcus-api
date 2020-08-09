package be.wouterversyck.marcusapi.linkpreview.controllers;

import be.wouterversyck.marcusapi.linkpreview.models.LinkPreview;
import be.wouterversyck.marcusapi.linkpreview.services.LinkPreviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("linkpreview")
public class LinkPreviewController {
    private final LinkPreviewService linkPreviewService;

    public LinkPreviewController(final LinkPreviewService linkPreviewService) {
        this.linkPreviewService = linkPreviewService;
    }

    @GetMapping
    public LinkPreview getLinkPreview(@RequestParam("q") final String link) {
        return linkPreviewService.getLinkPreview(link);
    }
}
