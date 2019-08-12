package be.wouterversyck.shoppinglistapi.security.models;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
