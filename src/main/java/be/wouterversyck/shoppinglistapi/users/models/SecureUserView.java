package be.wouterversyck.shoppinglistapi.users.models;

public interface SecureUserView {
    long getId();
    String getUsername();
    Role getRole();
}
