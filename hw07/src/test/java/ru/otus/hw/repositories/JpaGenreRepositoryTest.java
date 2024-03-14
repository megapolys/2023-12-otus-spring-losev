package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.entity.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataJpaTest
@Import({JpaGenreRepository.class})
public class JpaGenreRepositoryTest {

	@Autowired
	private JpaGenreRepository repositoryJpa;

	@Autowired
	private TestEntityManager em;

	@DisplayName("должен загружать список всех жанров")
	@Test
	void shouldReturnCorrectGenresList() {
		var actualGenres = repositoryJpa.findAll();
		var expectedGenres = getDbGenres();

		assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
		actualGenres.forEach(System.out::println);
	}

	private List<Genre> getDbGenres() {
		return IntStream.range(1, 7).mapToObj(i -> em.find(Genre.class, i)).toList();
	}

	@DisplayName("должен загружать список жанров по идентификаторам")
	@Test
	void shouldReturnCorrectGenresListByIds() {
		var actualGenres = repositoryJpa.findAllByIds(Set.of(1L, 2L, 3L));
		var expectedGenres = getDbGenresByIds();

		assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
	}

	private List<Genre> getDbGenresByIds() {
		return IntStream.range(1, 4).mapToObj(i -> em.find(Genre.class, i)).toList();
	}
}
