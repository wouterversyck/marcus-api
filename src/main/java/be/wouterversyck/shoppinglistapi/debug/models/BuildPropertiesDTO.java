package be.wouterversyck.shoppinglistapi.debug.models;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class BuildPropertiesDTO {
    private String group;
    private String artifact;
    private String version;
    private String javaVersion;
    private Instant time;
    private String name;
}
