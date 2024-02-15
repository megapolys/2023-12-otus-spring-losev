package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;

import java.util.List;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

	private final GenreRepository genreRepository;

	@PersistenceContext
	private final EntityManager em;

	@Override
	public Book findById(long id) {
		var graph = em.getEntityGraph("book-graph");
		TypedQuery<Book> booksQuery = em.createQuery("select b from Book b where b.id = :id", Book.class);
		booksQuery.setParameter("id", id);
		booksQuery.setHint(FETCH.getKey(), graph);
		return booksQuery.getSingleResult();
	}

	@Override
	public List<Book> findAll() {
		var graph = em.getEntityGraph("book-graph");
		TypedQuery<Book> booksQuery = em.createQuery("select b from Book b", Book.class);
		booksQuery.setHint(FETCH.getKey(), graph);
		return booksQuery.getResultList();
	}

	@Override
	@Transactional
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
