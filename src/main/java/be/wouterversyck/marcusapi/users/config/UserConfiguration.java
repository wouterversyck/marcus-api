package be.wouterversyck.marcusapi.users.config;

import be.wouterversyck.marcusapi.users.persistence.RolesDao;
import be.wouterversyck.marcusapi.users.persistence.UserDao;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = { UserDao.class, RolesDao.class })
public class UserConfiguration {
}
