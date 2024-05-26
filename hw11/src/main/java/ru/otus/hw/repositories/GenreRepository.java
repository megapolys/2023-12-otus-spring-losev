package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.entity.Genre;

import java.util.Set;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

	@Query("{'id': { $in:  [:#{#ids}]}}")
	Flux<Genre> findAllByIds(@Param("ids") Set<String> ids);
}
