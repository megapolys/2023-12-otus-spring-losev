package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

	private final GenreRepository genreRepository;

	@PersistenceContext
	private final EntityManager em;

	@Override
	public Optional<Book> findById(long id) {
		return Optional.ofNullable(em.find(Book.class, id));
	}

	@Override
	public List<Book> findAll() {
		var graph = em.getEntityGraph("book-graph");
		TypedQuery<Book> booksQuery = em.createQuery("select b from Book b", Book.class);
		booksQuery.setHint(FETCH.getKey(), graph);
		return booksQuery.getResultList();
	}

	@Override
	public Book save(Book book) {
		if (book.getId() == 0) {
			em.persist(book);
			return book;
		}
		return em.merge(book);
	}

	@Override
	public void deleteById(long id) {
		Book book = em.find(Book.class, id);
		if (book != null) {
			em.remove(book);
		}
	}
}
