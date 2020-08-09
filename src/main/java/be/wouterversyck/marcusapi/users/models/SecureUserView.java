package be.wouterversyck.marcusapi.users.models;

public interface SecureUserView {
    long getId();
    String getUsername();
    String getEmail();
    Role getRole();
}
