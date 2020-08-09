package be.wouterversyck.marcusapi.debug.controllers;

import be.wouterversyck.marcusapi.debug.models.BuildPropertiesDTO;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/version")
public class VersionController {

    private final BuildPropertiesDTO buildProperties;

    public VersionController(final BuildProperties buildProperties) {
        this.buildProperties = BuildPropertiesDTO.builder()
                .artifact(buildProperties.getArtifact())
                .group(buildProperties.getGroup())
                .name(buildProperties.getName())
                .time(buildProperties.getTime())
                .javaVersion(buildProperties.get("java.target"))
                .version(buildProperties.getVersion())
                .build();
    }

    @GetMapping
    public BuildPropertiesDTO getBuildInfo() {
        return buildProperties;
    }
}
