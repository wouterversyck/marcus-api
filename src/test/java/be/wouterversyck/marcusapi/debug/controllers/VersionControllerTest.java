package be.wouterversyck.marcusapi.debug.controllers;

import be.wouterversyck.marcusapi.debug.models.BuildPropertiesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.info.BuildProperties;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VersionControllerTest {

    private VersionController versionController;

    private static final String GROUP = "GROUP";
    private static final String ARTIFACT = "ARTIFACT";
    private static final String VERSION = "VERSION";
    private static final String JAVA_VERSION = "JAVA_VERSION";
    private static final String NAME = "NAME";
    private static final Instant TIME = Instant.now();

    @BeforeEach
    void setup() {
        BuildProperties buildProperties = mock(BuildProperties.class);
        when(buildProperties.getGroup()).thenReturn(GROUP);
        when(buildProperties.getArtifact()).thenReturn(ARTIFACT);
        when(buildProperties.getVersion()).thenReturn(VERSION);
        when(buildProperties.get("java.target")).thenReturn(JAVA_VERSION);
        when(buildProperties.getTime()).thenReturn(TIME);
        when(buildProperties.getName()).thenReturn(NAME);

        versionController = new VersionController(buildProperties);
    }

    @Test
    void shouldReturnCorrectInformation_WhenEndpointIsCalled() {
        BuildPropertiesDTO result = versionController.getBuildInfo();

        assertThat(result.getGroup()).isEqualTo(GROUP);
        assertThat(result.getArtifact()).isEqualTo(ARTIFACT);
        assertThat(result.getVersion()).isEqualTo(VERSION);
        assertThat(result.getJavaVersion()).isEqualTo(JAVA_VERSION);
        assertThat(result.getTime()).isEqualTo(TIME);
        assertThat(result.getName()).isEqualTo(NAME);
    }
}
