package be.wouterversyck.shoppinglistapi.users.config;

import be.wouterversyck.shoppinglistapi.users.persistence.RolesDao;
import be.wouterversyck.shoppinglistapi.users.persistence.UserDao;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = { UserDao.class, RolesDao.class })
public class UserConfiguration {
}
