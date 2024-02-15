package ru.otus.hw.repositories;

import jakarta.persistence.NoResultException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class})
class JdbcBookRepositoryTest {

	@Autowired
	private JpaBookRepository repositoryJpa;

	@Autowired
	private TestEntityManager em;

	private List<Book> dbBooks;

	@BeforeEach
	void setUp() {
		dbBooks = getDbBooks();
	}

	private List<Book> getDbBooks() {
		return IntStream.range(1, 4).mapToObj(i -> em.find(Book.class, i)).toList();
	}

	private static Stream<Arguments> getExpectedBooks() {
		return LongStream.range(1, 4).mapToObj(Arguments::of);
	}

	@DisplayName("должен загружать книгу по id")
	@ParameterizedTest
	@MethodSource("getExpectedBooks")
	void shouldReturnCorrectBookById(Long expectedBookId) {
		Book expectedBook = em.find(Book.class, expectedBookId);
		Book actualBook = repositoryJpa.findById(expectedBookId);
		assertThat(actualBook).isEqualTo(expectedBook);
	}

	@DisplayName("должен вернуть корректное значение, если книга не найдена")
	@Test
	void ifBookNotFoundShouldReturnException() {
		Assertions.assertThatExceptionOfType(NoResultException.class).isThrownBy(() -> repositoryJpa.findById(4));
	}

	@DisplayName("должен загружать список всех книг")
	@Test
	void shouldReturnCorrectBooksList() {
		var actualBooks = repositoryJpa.findAll();
		var expectedBooks = dbBooks;

		assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
		actualBooks.forEach(System.out::println);
	}

	@DisplayName("должен сохранять новую книгу")
	@Sql(scripts = "sql/insert-author.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	void shouldSaveNewBook() {
		var expectedBook = new Book(4, "BookTitle_10500", em.find(Author.class, 4),
			List.of(em.find(Genre.class, 1), em.find(Genre.class, 3))
		);
		var returnedBook = repositoryJpa.save(expectedBook);
		assertThat(returnedBook).isNotNull()
			.matches(book -> book.getId() > 0)
			.usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

		assertThat(em.find(Book.class, returnedBook.getId())).isEqualTo(returnedBook);
	}

	@DisplayName("должен сохранять измененную книгу")
	@Sql(scripts = "sql/insert-author.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	void shouldSaveUpdatedBook() {
		var expectedBook = new Book(1L, "BookTitle_10500", em.find(Author.class, 4),
			List.of(em.find(Genre.class, 4), em.find(Genre.class, 5))
		);

		assertThat(em.find(Book.class, expectedBook.getId())).isNotEqualTo(expectedBook);

		var returnedBook = repositoryJpa.save(expectedBook);
		assertThat(returnedBook).isNotNull()
			.matches(book -> book.getId() > 0)
			.usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

		assertThat(em.find(Book.class, returnedBook.getId())).isEqualTo(returnedBook);
	}

	@DisplayName("должен удалять книгу по id ")
	@Test
	void shouldDeleteBook() {
		assertThat(em.find(Book.class, 1L)).isNotNull();
		repositoryJpa.deleteById(1L);
		assertThat(em.find(Book.class, 1L)).isNull();
	}
}