package be.wouterversyck.marcusapi.users.models;

import java.util.Arrays;

public enum Role {
    ADMIN(1),
    USER(2);

    private final Integer id;

    Role(final Integer id) {
        this.id = id;
    }

    public static Role fromId(final Integer id) {
        return Arrays.stream(values())
                .filter(role -> role.id.equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("Role not found for id: [%s]", id)));
    }

    public Integer getId() {
        return id;
    }
}
