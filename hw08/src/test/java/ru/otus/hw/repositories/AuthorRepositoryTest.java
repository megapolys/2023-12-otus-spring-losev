package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.entity.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами ")
@DataMongoTest
public class AuthorRepositoryTest {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setup() {
		mongoTemplate.dropCollection("authors");
		mongoTemplate.insert(new Author("1", "Author_1"));
		mongoTemplate.insert(new Author("2", "Author_2"));
	}

	@DisplayName("должен загружать автора по идентификатору")
	@Test
	void shouldReturnCorrectAuthorById() {
		Author expectedAuthor = new Author("1", "Author_1");
		var actualAuthor = authorRepository.findById("1");

		assertThat(actualAuthor.isPresent()).isTrue();
		assertThat(actualAuthor.get()).isEqualTo(expectedAuthor);
	}

	@DisplayName("должен сохранять нового автора")
	@Test
	void shouldCorrectSaveAuthor() {
		Author expected = authorRepository.save(new Author("new_author"));
		Author actual = mongoTemplate.findById(expected.getId(), Author.class);

		assertThat(actual).isEqualTo(expected);
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
