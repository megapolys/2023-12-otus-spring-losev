package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

	@EntityGraph("book-author-genres-graph")
	Optional<Book> findById(Long aLong);

	@EntityGraph("book-author-graph")
	List<Book> findAll();
}
