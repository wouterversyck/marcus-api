package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.users.daos.UserDao;
import be.wouterversyck.shoppinglistapi.users.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserDao userDao;

    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByUsername(final String username) {
        return userDao.findByUsername(username);
    }

}
