package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

	private final GenreRepository genreRepository;

	private final NamedParameterJdbcOperations jdbc;

	@Override
	public Optional<Book> findById(long id) {
		return Optional.ofNullable(jdbc.query(
			"select b.id as id, b.title as title, a.id as author_id, a.full_name as author_fullname, " +
				"g.id as genre_id, g.name as genre_name " +
				"from books as b " +
				"left join authors as a on b.author_id = a.id " +
				"left join books_genres as bg on bg.book_id = b.id " +
				"left join genres as g on bg.genre_id = g.id " +
				"where b.id = :id",
			new MapSqlParameterSource("id", id),
			new BookResultSetExtractor()
		));
	}

	@Override
	public List<Book> findAll() {
		var genres = genreRepository.findAll();
		var relations = getAllGenreRelations();
		var books = getAllBooksWithoutGenres();
		mergeBooksInfo(books, genres, relations);
		return books;
	}

	@Override
	public Book save(Book book) {
		if (book.getId() == 0) {
			return insert(book);
		}
		return update(book);
	}

	@Override
	public void deleteById(long id) {
		jdbc.update("delete from books where id = :id", new MapSqlParameterSource("id", id));
	}

	private List<Book> getAllBooksWithoutGenres() {
		return jdbc.query(
			"select b.id as id, b.title as title, a.id as author_id, a.full_name as author_fullname " +
				"from books as b " +
				"left join authors as a on b.author_id = a.id ",
			new BookRowMapper()
		);
	}

	private List<BookGenreRelation> getAllGenreRelations() {
		return jdbc.query("select book_id, genre_id from books_genres", new BookGenreRelationMapper());
	}

	private void mergeBooksInfo(
		List<Book> booksWithoutGenres,
		List<Genre> genres,
		List<BookGenreRelation> relations
	) {
		Map<Long, Genre> genresByGenreId = genres.stream()
			.collect(Collectors.toMap(Genre::getId, g -> g));
		Map<Long, List<BookGenreRelation>> relationsByBookId = relations.stream()
			.collect(Collectors.groupingBy(BookGenreRelation::bookId));
		for (Book book : booksWithoutGenres) {
			List<Genre> genresByBook = relationsByBookId.get(book.getId()).stream()
				.map(r -> genresByGenreId.get(r.genreId))
				.toList();
			if (genresByBook.isEmpty()) {
				throw new EntityNotFoundException("Genre entity not found");
			}
			book.setGenres(genresByBook);
		}
	}

	private Book insert(Book book) {
		var keyHolder = new GeneratedKeyHolder();
		jdbc.update(
			"insert into books(title, author_id) values(:title, :author_id)",
			new MapSqlParameterSource(Map.of("title", book.getTitle(),
				"author_id", book.getAuthor().getId()
			)),
			keyHolder
		);
		//noinspection DataFlowIssue
		book.setId(keyHolder.getKeyAs(Long.class));
		batchInsertGenresRelationsFor(book);
		return book;
	}

	private Book update(Book book) {
		int updatedRowsCount = jdbc.update(
			"update books " +
				"set title = :title," +
				"author_id = :author_id " +
				"where id = :id",
			new MapSqlParameterSource(Map.of(
				"title", book.getTitle(),
				"author_id", book.getAuthor().getId(),
				"id", book.getId()
			))
		);
		if (updatedRowsCount == 0) {
			throw new EntityNotFoundException("Book for update not found");
		}

		removeGenresRelationsForBookId(book.getId());
		batchInsertGenresRelationsFor(book);

		return book;
	}

	private void batchInsertGenresRelationsFor(Book book) {
		jdbc.batchUpdate(
			"insert into books_genres(book_id, genre_id) values(:book_id, :genre_id)",
			book.getGenres().stream()
				.map(genre -> new MapSqlParameterSource(Map.of(
					"book_id", book.getId(),
					"genre_id", genre.getId()
				)))
				.toList()
				.toArray(new MapSqlParameterSource[]{})
		);
	}

	private void removeGenresRelationsForBookId(long bookId) {
		jdbc.update(
			"delete from books_genres where book_id = :bookId",
			new MapSqlParameterSource("bookId", bookId)
		);
	}

	private static class BookRowMapper implements RowMapper<Book> {

		@Override
		public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
			return Book.builder()
				.id(rs.getLong("id"))
				.title(rs.getString("title"))
				.author(Author.builder()
					.id(rs.getLong("author_id"))
					.fullName(rs.getString("author_fullname"))
					.build())
				.build();
		}
	}

	private static class BookGenreRelationMapper implements RowMapper<BookGenreRelation> {

		@Override
		public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
		}
	}

	// Использовать для findById
	@SuppressWarnings("ClassCanBeRecord")
	@RequiredArgsConstructor
	private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

		@Override
		public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
			Book book = null;
			while (rs.next()) {
				if (book == null) {
					book = Book.builder()
						.id(rs.getLong("id"))
						.title(rs.getString("title"))
						.author(Author.builder()
							.id(rs.getLong("author_id"))
							.fullName(rs.getString("author_fullname"))
							.build())
						.genres(new ArrayList<>())
						.build();
				}
				book.getGenres().add(Genre.builder()
					.id(rs.getLong("genre_id"))
					.name(rs.getString("genre_name"))
					.build());
			}
			return book;
		}
	}

	private record BookGenreRelation(long bookId, long genreId) {
	}
}
