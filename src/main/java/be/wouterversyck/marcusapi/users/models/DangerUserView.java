package be.wouterversyck.marcusapi.users.models;

/*
    Should never exit application, to be used internally
 */
public interface DangerUserView {
    long getId();
    String getUsername();
    String getPassword();
    Role getRole();
}
