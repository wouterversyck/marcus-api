package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.users.daos.UserDao;
import be.wouterversyck.shoppinglistapi.users.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

}
