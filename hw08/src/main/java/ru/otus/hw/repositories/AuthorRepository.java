package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.entity.Author;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String> {

	List<Author> findAll();
}
