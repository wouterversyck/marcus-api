package be.wouterversyck.shoppinglistapi.security.utils;

public final class SecurityConstants {

    public static final String AUTH_LOGIN_URL = "/login";

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String RESPONSE_TOKEN_HEADER = "X-Token";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
