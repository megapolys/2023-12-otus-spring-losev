package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами ")
@SpringBootTest
@AutoConfigureDataMongo
public class AuthorRepositoryTest {

	@Autowired
	private AuthorRepository authorRepository;

	@BeforeEach
	void setup() {
		authorRepository.deleteAll();
		authorRepository.save(new Author("1", "Author_1"));
		authorRepository.save(new Author("2", "Author_2"));
	}

	@DisplayName("должен загружать автора по идентификатору")
	@Test
	void shouldReturnCorrectAuthorById() {
		Author expectedAuthor = new Author("1", "Author_1");
		var actualAuthor = authorRepository.findById("1");

		assertThat(actualAuthor.isPresent()).isTrue();
		assertThat(actualAuthor.get()).isEqualTo(expectedAuthor);
	}

	@DisplayName("должен сохранять и получать нового автора")
	@Test
	void shouldCorrectSaveAndFindAuthor() {
		Author expected = authorRepository.save(new Author("new_author"));
		Optional<Author> actual = authorRepository.findById(expected.getId());

		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isEqualTo(expected);
	}
}
