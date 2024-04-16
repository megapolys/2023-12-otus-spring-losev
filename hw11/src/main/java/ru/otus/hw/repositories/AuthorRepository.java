package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.entity.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

	Mono<Boolean> existsByFullNameAndIdNot(String fullName, String id);
}
