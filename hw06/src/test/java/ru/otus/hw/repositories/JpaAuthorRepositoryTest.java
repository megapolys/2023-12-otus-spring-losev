package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
@Import({JpaAuthorRepository.class})
public class JpaAuthorRepositoryTest {

	@Autowired
	private JpaAuthorRepository repositoryJpa;

	@Autowired
	private TestEntityManager em;

	@DisplayName("должен загружать список всех авторов")
	@Test
	void shouldReturnCorrectAuthorsList() {
		var actualAuthors = repositoryJpa.findAll();
		var expectedAuthors = getDbAuthors();

		assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
		actualAuthors.forEach(System.out::println);
	}

	private List<Author> getDbAuthors() {
		return IntStream.range(1, 4).mapToObj(i -> em.find(Author.class, i)).toList();
	}

	@DisplayName("должен загружать автора по идентификатору")
	@Test
	void shouldReturnCorrectAuthorById() {
		var actualAuthor = repositoryJpa.findById(1L);
		var expectedAuthor = em.find(Author.class, 1L);

		assertThat(actualAuthor).isEqualTo(expectedAuthor);
	}
}
