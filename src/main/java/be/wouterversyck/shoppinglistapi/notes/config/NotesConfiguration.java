package be.wouterversyck.shoppinglistapi.notes.config;

import be.wouterversyck.shoppinglistapi.notes.persistence.ShoppingListDao;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = { ShoppingListDao.class })
public class NotesConfiguration {
}
