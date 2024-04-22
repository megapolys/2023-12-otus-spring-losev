package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами ")
@DataJpaTest
class JpaBookRepositoryTest {

	@Autowired
	private BookRepository bookRepository;

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
		Optional<Book> actualBook = bookRepository.findById(expectedBookId);
		assertThat(actualBook).isPresent()
			.get()
			.isEqualTo(expectedBook);
	}

	@DisplayName("должен вернуть корректное значение, если книга не найдена")
	@Test
	void ifBookNotFoundShouldReturnEmptyOptional() {
		Optional<Book> actualBook = bookRepository.findById(4L);
		assertThat(actualBook).isEmpty();
	}

	@DisplayName("должен загружать список всех книг")
	@Test
	void shouldReturnCorrectBooksList() {
		var actualBooks = bookRepository.findAll();
		var expectedBooks = dbBooks;

		assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
	}

	@DisplayName("должен сохранять новую книгу")
	@Test
	void shouldSaveNewBook() {
		Book book = new Book(4, "BookTitle_10500", em.find(Author.class, 4),
			List.of(em.find(Genre.class, 1), em.find(Genre.class, 3))
		);
		Book expectedBook = bookRepository.save(book);
		Book actualBook = em.find(Book.class, expectedBook.getId());

		assertThat(actualBook).isNotNull()
			.matches(b -> b.getId() > 0)
			.usingRecursiveComparison()
			.ignoringExpectedNullFields()
			.isEqualTo(expectedBook);
	}

	@DisplayName("должен сохранять и возвращать новую книгу")
	@Test
	void shouldSaveAndFindNewBook() {
		Book book = new Book(4, "BookTitle_10500", em.find(Author.class, 4),
			List.of(em.find(Genre.class, 1), em.find(Genre.class, 3))
		);
		Book expectedBook = bookRepository.save(book);
		Optional<Book> actualBook = bookRepository.findById(expectedBook.getId());

		assertThat(actualBook.isPresent()).isTrue();
		assertThat(actualBook.get()).isNotNull()
			.matches(b -> b.getId() > 0)
			.usingRecursiveComparison()
			.ignoringExpectedNullFields()
			.isEqualTo(expectedBook);
	}

	@DisplayName("должен сохранять измененную книгу")
	@Test
	void shouldSaveUpdatedBook() {
		Book book = new Book(1L, "BookTitle_10500", em.find(Author.class, 4),
			List.of(em.find(Genre.class, 4), em.find(Genre.class, 5))
		);

		assertThat(em.find(Book.class, book.getId())).isNotEqualTo(book);

		Book expectedBook = bookRepository.save(book);
		Book actualBook = em.find(Book.class, expectedBook.getId());

		assertThat(actualBook).isNotNull()
			.matches(b -> b.getId() > 0)
			.usingRecursiveComparison()
			.ignoringExpectedNullFields()
			.isEqualTo(expectedBook);
	}
}