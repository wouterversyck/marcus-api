package be.wouterversyck.shoppinglistapi.security.models;

import lombok.Data;

@Data
public class GoogleOauthRequest {
    private String idToken;
}
