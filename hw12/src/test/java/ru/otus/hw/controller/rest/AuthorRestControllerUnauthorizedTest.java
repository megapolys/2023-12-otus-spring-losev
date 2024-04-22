package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.controller.AbstractControllerTest;
import ru.otus.hw.controller.advice.RestControllerErrorHandler;
import ru.otus.hw.converters.MethodArgumentNotValidExceptionToValidationErrorDtoConverter;
import ru.otus.hw.generator.AuthorFormDtoGenerator;
import ru.otus.hw.models.dto.AuthorFormDto;
import ru.otus.hw.services.AuthorService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {
	AuthorRestController.class,
	RestControllerErrorHandler.class,
	MethodArgumentNotValidExceptionToValidationErrorDtoConverter.class
})
public class AuthorRestControllerUnauthorizedTest extends AbstractControllerTest {

	@MockBean
	private AuthorService authorService;

	@Test
	void whenGetAuthorsThenRedirect() throws Exception {
		mockMvc.perform(get("/api/v1/authors"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	void whenGetAuthorThenRedirect() throws Exception {
		mockMvc.perform(get("/api/v1/authors/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	void whenAddAuthorThenRedirect() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generateNewAuthor();

		mockMvc.perform(post("/api/v1/authors")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(author))
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	void whenEditAuthorThenRedirect() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generate();

		mockMvc.perform(put("/api/v1/authors/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(author))
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	void whenDeleteAuthorThenRedirect() throws Exception {
		mockMvc.perform(delete("/api/v1/authors/1")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}
}
