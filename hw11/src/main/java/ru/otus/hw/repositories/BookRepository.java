package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

	Mono<Boolean> existsBookByAuthor(Author author);
}
