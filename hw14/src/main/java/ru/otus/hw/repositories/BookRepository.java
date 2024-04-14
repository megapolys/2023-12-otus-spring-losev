package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.jpa.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

	@EntityGraph("book-author-genres-graph")
	List<Book> findAll();
}
