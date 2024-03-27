package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.entity.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с жанрами ")
@DataJpaTest
public class JpaGenreRepositoryTest {

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private TestEntityManager em;

	@DisplayName("должен загружать список всех жанров")
	@Test
	void shouldReturnCorrectGenresList() {
		var actualGenres = genreRepository.findAll();
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
		var actualGenres = genreRepository.findAllByIds(Set.of(1L, 2L, 3L));
		var expectedGenres = getDbGenresByIds();

		assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
	}

	private List<Genre> getDbGenresByIds() {
		return IntStream.range(1, 4).mapToObj(i -> em.find(Genre.class, i)).toList();
	}

	@DisplayName("должен загружать жанр по идентификатору")
	@Test
	void shouldReturnCorrectGenreById() {
		Optional<Genre> actual = genreRepository.findById(1L);
		Genre expected = em.find(Genre.class, 1);

		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isEqualTo(expected);
	}

	@DisplayName("должен сохранять жанр")
	@Test
	void shouldCorrectSaveGenre() {
		Genre expectedGenre = genreRepository.save(new Genre(0, "new_genre"));
		Genre actualGenre = em.find(Genre.class, expectedGenre.getId());

		assertThat(actualGenre).isNotNull();
		assertThat(actualGenre).isEqualTo(expectedGenre);
	}

	@DisplayName("должен сохранять и получить жанр")
	@Test
	void shouldCorrectSaveAndFindGenre() {
		Genre expectedGenre = genreRepository.save(new Genre(0, "new_genre"));
		Optional<Genre> actualGenre = genreRepository.findById(expectedGenre.getId());


		assertThat(actualGenre.isPresent()).isTrue();
		assertThat(actualGenre.get()).isEqualTo(expectedGenre);
	}
}
