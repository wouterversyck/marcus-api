package be.wouterversyck.marcusapi.users.exceptions;

import static java.lang.String.format;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(final long id) {
        super(format("User with id %s could not be found", id));
    }

    public UserNotFoundException(final String username) {
        super(format("User with username %s could not be found", username));
    }
}
