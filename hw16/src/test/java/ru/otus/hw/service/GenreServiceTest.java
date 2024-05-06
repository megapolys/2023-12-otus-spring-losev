package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.generator.GenreDtoGenerator;
import ru.otus.hw.generator.GenreGenerator;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.entity.Genre;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = GenreServiceImpl.class)
public class GenreServiceTest extends AbstractServiceTest {

	@Autowired
	private GenreService genreService;

	@Test
	void whenFindAllThenReturn() {
		List<Genre> genres = GenreGenerator.generateList();
		List<GenreDto> genreDtos = GenreDtoGenerator.generateList();

		when(genreRepository.findAll()).thenReturn(genres);

		List<GenreDto> actual = genreService.findAll();

		then(actual).isEqualTo(genreDtos);

		verify(genreRepository).findAll();
	}

}
