package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookGenreRelation;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

	private final GenreRepository genreRepository;

	@PersistenceContext
	private final EntityManager em;

	@Override
	public Optional<Book> findById(long id) {
		var graph = em.getEntityGraph("book-graph");
		TypedQuery<Book> booksQuery =
			em.createQuery("select b from Book b, Author a where b.author = a and b.id = :id", Book.class);
		booksQuery.setParameter("id", id);
		booksQuery.setHint(FETCH.getKey(), graph);
		try {
			Book book = booksQuery.getSingleResult();
			return Optional.ofNullable(book);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Book> findAll() {
		TypedQuery<Book> booksQuery = em.createQuery("select b from Book b", Book.class);
		List<Book> books = booksQuery.getResultList();
		var genres = genreRepository.findAll();
		var relations = getAllGenreRelations();
		mergeBooksInfo(books, genres, relations);
		return books;
	}


	private List<BookGenreRelation> getAllGenreRelations() {
		TypedQuery<BookGenreRelation> query = em.createQuery(
			"select new ru.otus.hw.models.BookGenreRelation(b.id, g.id) from Book b, Genre g",
			BookGenreRelation.class
		);
		return query.getResultList();
	}

	private void mergeBooksInfo(
		List<Book> booksWithoutGenres,
		List<Genre> genres,
		List<BookGenreRelation> relations
	) {
		Map<Long, Genre> genresByGenreId = genres.stream()
			.collect(Collectors.toMap(Genre::getId, g -> g));
		Map<Long, List<BookGenreRelation>> relationsByBookId = relations.stream()
			.collect(Collectors.groupingBy(BookGenreRelation::getBookId));
		for (Book book : booksWithoutGenres) {
			List<Genre> genresByBook = relationsByBookId.get(book.getId()).stream()
				.map(r -> genresByGenreId.get(r.getGenreId()))
				.toList();
			if (genresByBook.isEmpty()) {
				throw new EntityNotFoundException("Genre entity not found");
			}
			book.setGenres(genresByBook);
		}
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
