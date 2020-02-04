package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.users.daos.UserDao;
import be.wouterversyck.shoppinglistapi.users.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String USERNAME = "USERNAME";

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnUser_WhenIdIsPassed() {
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
