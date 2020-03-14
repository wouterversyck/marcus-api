package be.wouterversyck.shoppinglistapi.security.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GoogleOauthRequest {
    @NotBlank(message = "Id token must be provided")
    private String idToken;
}
