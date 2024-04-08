package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.controller.AbstractControllerTest;
import ru.otus.hw.services.GenreService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = GenreRestController.class)
public class GenreRestControllerSecurityTest extends AbstractControllerTest {

	@MockBean
	private GenreService genreService;

	@Test
	void whenGetGenresThenRedirect() throws Exception {
		mockMvc.perform(get("/api/v1/genres"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser(roles = {})
	void whenGetGenresThenForbidden() throws Exception {
		mockMvc.perform(get("/api/v1/genres"))
			.andExpect(status().isForbidden());
	}
}