package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.entity.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

	@PersistenceContext
	private final EntityManager em;

	@Override
	public Optional<Book> findById(long id) {
		EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genres-graph");
		Map<String, Object> properties = new HashMap<>();
		properties.put(FETCH.getKey(), entityGraph);
		return Optional.ofNullable(em.find(Book.class, id, properties));
	}

	@Override
	public List<Book> findAll() {
		EntityGraph<?> entityGraph = em.getEntityGraph("book-author-graph");
		TypedQuery<Book> query = em.createQuery(
			"select b from Book b", Book.class
		);
		query.setHint(FETCH.getKey(), entityGraph);
		return query.getResultList();
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
