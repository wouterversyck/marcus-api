package be.wouterversyck.shoppinglistapi.security.models;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String password;
    private String token;
}
