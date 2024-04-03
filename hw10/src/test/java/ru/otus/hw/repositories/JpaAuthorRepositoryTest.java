package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.entity.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами ")
@DataJpaTest
public class JpaAuthorRepositoryTest {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private TestEntityManager em;

	@DisplayName("должен загружать автора по идентификатору")
	@Test
	void shouldReturnCorrectAuthorById() {
		Author expectedAuthor = new Author(1L, "Author_1");
		var actualAuthor = authorRepository.findById(1L);

		assertThat(actualAuthor.isPresent()).isTrue();
		assertThat(actualAuthor.get()).isEqualTo(expectedAuthor);
	}

	@DisplayName("должен сохранять нового автора")
	@Test
	void shouldCorrectSaveAuthor() {
		Author expected = authorRepository.save(new Author(0, "new_author"));

		Author actual = em.find(Author.class, expected.getId());
		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("должен сохранять и получать нового автора")
	@Test
	void shouldCorrectSaveAndFindAuthor() {
		Author expected = authorRepository.save(new Author(0, "new_author"));
		Optional<Author> actual = authorRepository.findById(expected.getId());

		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isEqualTo(expected);
	}
}
