package be.wouterversyck.shoppinglistapi.security.models;

import lombok.Data;

@Data
public class LoginRequest {
    // can be username of email
    private String username;
    private String password;
}
