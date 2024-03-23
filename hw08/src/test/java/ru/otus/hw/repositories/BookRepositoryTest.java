package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.repositories.generator.BookGenerator;

import java.util.Optional;
import java.util.Set;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами ")
@SpringBootTest
@AutoConfigureDataMongo
class BookRepositoryTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@BeforeEach
	void setup() {
		bookRepository.deleteAll();
		bookRepository.save(new Book("1", "BookTitle_1", authorRepository.findById("1").get(),
			Set.of("Genre_1", "Genre_2")));
		bookRepository.save(new Book("2", "BookTitle_2", authorRepository.findById("1").get(),
			Set.of("Genre_3", "Genre_4")));
		bookRepository.save(new Book("3", "BookTitle_3", authorRepository.findById("2").get(),
			Set.of("Genre_5", "Genre_6")));
	}

	private static Stream<Arguments> getExpectedBooks() {
		return LongStream.range(1, 4).mapToObj(i -> Arguments.of(String.valueOf(i)));
	}

	@DisplayName("должен загружать книгу по id")
	@ParameterizedTest
	@MethodSource("getExpectedBooks")
	void shouldReturnCorrectBookById(String expectedBookId) {
		Optional<Book> optionalBook = bookRepository.findById(expectedBookId);
		assertThat(optionalBook.isPresent()).isTrue();
		Book expectedBook = optionalBook.get();
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
		Book book = BookGenerator.generateUpdate();

		assertThat(bookRepository.findById(book.getId())).isPresent().get().isNotEqualTo(book);

		Book expectedBook = bookRepository.save(book);
		Optional<Book> actualBook = bookRepository.findById(expectedBook.getId());

		assertThat(actualBook).isPresent().get()
			.usingRecursiveComparison()
			.ignoringExpectedNullFields()
			.isEqualTo(expectedBook);
	}
}