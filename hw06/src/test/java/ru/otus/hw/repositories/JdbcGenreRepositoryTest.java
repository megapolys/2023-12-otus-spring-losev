package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами ")
@SpringBootTest
public class JdbcGenreRepositoryTest {

	@Autowired
	JpaGenreRepository genreRepository;

	private List<Genre> dbGenres;

	private List<Genre> dbGenresByIds;

	@BeforeEach
	void setUp() {
		dbGenres = getDbGenres();
		dbGenresByIds = getDbGenresByIds();
	}


	@DisplayName("должен загружать список всех книг")
	@Test
	void shouldReturnCorrectBooksList() {
		var actualGenres = genreRepository.findAll();
		var expectedGenres = dbGenres;

		assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
		actualGenres.forEach(System.out::println);
	}


	@DisplayName("должен загружать список книг по идентификаторам")
	@Test
	void shouldReturnCorrectBooksListByIds() {
		var actualGenres = genreRepository.findAllByIds(Set.of(1L, 2L, 3L));
		var expectedGenres = dbGenresByIds;

		assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
		actualGenres.forEach(System.out::println);
	}

	private static List<Genre> getDbGenres() {
		return IntStream.range(1, 7).boxed()
			.map(id -> new Genre(id, "Genre_" + id))
			.toList();
	}

	private static List<Genre> getDbGenresByIds() {
		return IntStream.range(1, 4).boxed()
			.map(id -> new Genre(id, "Genre_" + id))
			.toList();
	}
}
