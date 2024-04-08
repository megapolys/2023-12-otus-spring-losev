package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.controller.AbstractControllerTest;
import ru.otus.hw.generator.GenreDtoGenerator;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = GenreRestController.class)
@WithMockUser
public class GenreRestControllerTest extends AbstractControllerTest {

	@MockBean
	private GenreService genreService;

	@Test
	void whenGetGenresThenSuccess() throws Exception {
		List<GenreDto> genreDtos = GenreDtoGenerator.generateList();

		when(genreService.findAll()).thenReturn(genreDtos);

		mockMvc.perform(get("/api/v1/genres"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(genreDtos.size())))
			.andExpect(jsonPath("$.[0].id", is(Long.valueOf(genreDtos.get(0).getId()).intValue())))
			.andExpect(jsonPath("$.[0].name", is(genreDtos.get(0).getName())))
			.andExpect(jsonPath("$.[1].id", is(Long.valueOf(genreDtos.get(1).getId()).intValue())))
			.andExpect(jsonPath("$.[1].name", is(genreDtos.get(1).getName())));

		verify(genreService).findAll();
	}
}