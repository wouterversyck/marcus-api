package be.wouterversyck.shoppinglistapi.debug.controllers;

import be.wouterversyck.shoppinglistapi.debug.models.BuildPropertiesDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.info.BuildProperties;

import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class VersionControllerTest {

    @Mock
    private BuildProperties buildProperties;

    private VersionController versionController;

    private static final String GROUP = "GROUP";
    private static final String ARTIFACT = "ARTIFACT";
    private static final String VERSION = "VERSION";
    private static final String JAVA_VERSION = "JAVA_VERSION";
    private static final String NAME = "NAME";
    private static final Instant TIME = Instant.now();

    @Before
    public void setup() {
        when(buildProperties.getGroup()).thenReturn(GROUP);
        when(buildProperties.getArtifact()).thenReturn(ARTIFACT);
        when(buildProperties.getVersion()).thenReturn(VERSION);
        when(buildProperties.get("java.target")).thenReturn(JAVA_VERSION);
        when(buildProperties.getTime()).thenReturn(TIME);
        when(buildProperties.getName()).thenReturn(NAME);

        versionController = new VersionController(buildProperties);
    }

    @Test
    public void shouldReturnCorrectInformation_WhenEndpointIsCalled() {
        BuildPropertiesDTO result = versionController.getBuildInfo();

        assertThat(result.getGroup()).isEqualTo(GROUP);
        assertThat(result.getArtifact()).isEqualTo(ARTIFACT);
        assertThat(result.getVersion()).isEqualTo(VERSION);
        assertThat(result.getJavaVersion()).isEqualTo(JAVA_VERSION);
        assertThat(result.getTime()).isEqualTo(TIME);
        assertThat(result.getName()).isEqualTo(NAME);
    }
}