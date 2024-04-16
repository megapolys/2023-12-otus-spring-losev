package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.entity.Genre;

import java.util.Set;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

	@Query("select g from Genre g where g.id in (:ids)")
	Flux<Genre> findAllByIds(Set<Long> ids);
}
