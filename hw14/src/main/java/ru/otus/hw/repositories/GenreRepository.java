package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.model.jpa.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	List<Genre> findAll();

	@Query("select g from Genre g where g.id in (:ids)")
	List<Genre> findAllByIds(Set<Long> ids);
}
