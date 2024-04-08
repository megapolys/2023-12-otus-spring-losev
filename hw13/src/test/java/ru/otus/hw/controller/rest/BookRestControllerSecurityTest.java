package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.controller.AbstractControllerTest;
import ru.otus.hw.controller.advice.RestControllerErrorHandler;
import ru.otus.hw.converters.MethodArgumentNotValidExceptionToValidationErrorDtoConverter;
import ru.otus.hw.generator.BookFormDtoGenerator;
import ru.otus.hw.models.dto.BookFormDto;
import ru.otus.hw.services.BookService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {
	BookRestController.class,
	RestControllerErrorHandler.class,
	MethodArgumentNotValidExceptionToValidationErrorDtoConverter.class
})
public class BookRestControllerSecurityTest extends AbstractControllerTest {

	@MockBean
	private BookService bookService;

	@Test
	void whenReturnBooksThenRedirect() throws Exception {
		mockMvc.perform(get("/api/v1/books"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser(roles = {})
	void whenReturnBooksThenForbidden() throws Exception {
		mockMvc.perform(get("/api/v1/books"))
			.andExpect(status().isForbidden());
	}

	@Test
	void whenReturnBookThenRedirect() throws Exception {
		mockMvc.perform(get("/api/v1/books/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser(roles = {})
	void whenReturnBookThenForbidden() throws Exception {
		mockMvc.perform(get("/api/v1/books/1"))
			.andExpect(status().isForbidden());
	}

	@Test
	void whenAddBookThenRedirect() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		mockMvc.perform(post("/api/v1/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book))
				.with(csrf())
			).andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser
	void whenAddBookThenForbidden() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		mockMvc.perform(post("/api/v1/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book))
				.with(csrf())
			).andExpect(status().isForbidden());
	}

	@Test
	void whenEditBookThenRedirect() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		mockMvc.perform(put("/api/v1/books/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(book))
			.with(csrf())
		).andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser
	void whenEditBookThenForbidden() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		mockMvc.perform(put("/api/v1/books/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(book))
			.with(csrf())
		).andExpect(status().isForbidden());
	}

	@Test
	void whenDeleteBookThenRedirect() throws Exception {
		mockMvc.perform(delete("/api/v1/books/1")
				.with(csrf())
			).andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser
	void whenDeleteBookThenForbidden() throws Exception {
		mockMvc.perform(delete("/api/v1/books/1")
				.with(csrf())
			).andExpect(status().isForbidden());
	}
}
