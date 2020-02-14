package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.users.persistence.UserDao;
import be.wouterversyck.shoppinglistapi.users.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String USERNAME = "USERNAME";

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUser_WhenIdIsPassed() {
        when(userDao.findByUsername(USERNAME)).thenReturn(createUser());

        User user = userService.getUserByUsername(USERNAME);

        assertThat(user.getUsername()).isEqualTo(USERNAME);
    }

    private User createUser() {
        User user = new User();
        user.setUsername(USERNAME);

        return user;
    }
}
