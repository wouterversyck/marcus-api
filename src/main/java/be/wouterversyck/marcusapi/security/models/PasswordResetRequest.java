package be.wouterversyck.marcusapi.security.models;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String password;
    private String token;
}
