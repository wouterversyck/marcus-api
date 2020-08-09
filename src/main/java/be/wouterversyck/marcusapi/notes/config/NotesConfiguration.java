package be.wouterversyck.marcusapi.notes.config;

import be.wouterversyck.marcusapi.notes.persistence.NotesDao;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = { NotesDao.class })
public class NotesConfiguration {
}
