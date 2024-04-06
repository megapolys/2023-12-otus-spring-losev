package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Genre;
import ru.otus.hw.repositories.generator.BookGenerator;

import java.util.Optional;
import java.util.Set;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами ")
@DataMongoTest
class BookRepositoryTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setup() {
		mongoTemplate.dropCollection("books");
		mongoTemplate.insert(new Book("1", "BookTitle_1", mongoTemplate.findById("1", Author.class),
			Set.of(new Genre("Genre_1"), new Genre("Genre_2"))));
		mongoTemplate.insert(new Book("2", "BookTitle_2", mongoTemplate.findById("1", Author.class),
			Set.of(new Genre("Genre_3"), new Genre("Genre_4"))));
		mongoTemplate.insert(new Book("3", "BookTitle_3", mongoTemplate.findById("2", Author.class),
			Set.of(new Genre("Genre_5"), new Genre("Genre_6"))));
	}

	private static Stream<Arguments> getExpectedBooks() {
		return LongStream.range(1, 4).mapToObj(i -> Arguments.of(String.valueOf(i)));
	}

	@DisplayName("должен загружать книгу по id")
	@ParameterizedTest
	@MethodSource("getExpectedBooks")
	void shouldReturnCorrectBookById(String expectedBookId) {
		Book expectedBook = mongoTemplate.findById(expectedBookId, Book.class);
		Optional<Book> actualBook = bookRepository.findById(expectedBookId);
		assertThat(actualBook).isPresent().get()
			.isEqualTo(expectedBook);
	}

	@DisplayName("должен вернуть корректное значение, если книга не найдена")
	@Test
	void ifBookNotFoundShouldReturnEmptyOptional() {
		Optional<Book> actualBook = bookRepository.findById("4");
		assertThat(actualBook).isEmpty();
	}

	@DisplayName("должен загружать список всех книг")
	@Test
	void shouldReturnCorrectBooksList() {
		var actualBooks = bookRepository.findAll();
		var expectedBooks = BookGenerator.generateList();

		assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
	}

	@DisplayName("должен сохранять новую книгу")
	@Test
	void shouldSaveNewBook() {
		Book book = BookGenerator.generateNew();
		Book expectedBook = bookRepository.save(book);
		Book actualBook = mongoTemplate.findById(expectedBook.getId(), Book.class);

		assertThat(actualBook).isEqualTo(expectedBook);
	}

	@DisplayName("должен сохранять и возвращать новую книгу")
	@Test
	void shouldSaveAndFindNewBook() {
		Book book = BookGenerator.generateNew();
		Book expectedBook = bookRepository.save(book);
		Optional<Book> actualBook = bookRepository.findById(expectedBook.getId());

		assertThat(actualBook).isPresent().get()
			.usingRecursiveComparison()
			.ignoringExpectedNullFields()
			.isEqualTo(expectedBook);
	}

	@DisplayName("должен сохранять измененную книгу")
	@Test
	void shouldSaveUpdatedBook() {
		Book expectedBook = BookGenerator.generateUpdate();

		assertThat(mongoTemplate.findById(expectedBook.getId(), Book.class)).isNotEqualTo(expectedBook);

		Book savedBook = bookRepository.save(expectedBook);
		Book actualBook = mongoTemplate.findById(savedBook.getId(), Book.class);

		assertThat(actualBook).isEqualTo(expectedBook);
	}
}