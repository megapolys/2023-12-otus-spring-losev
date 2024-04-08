package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.controller.AbstractControllerTest;
import ru.otus.hw.controller.advice.RestControllerErrorHandler;
import ru.otus.hw.converters.MethodArgumentNotValidExceptionToValidationErrorDtoConverter;
import ru.otus.hw.exceptions.DeleteEntityException;
import ru.otus.hw.exceptions.author.FullNameDuplicateException;
import ru.otus.hw.generator.AuthorDtoGenerator;
import ru.otus.hw.generator.AuthorFormDtoGenerator;
import ru.otus.hw.generator.AuthorGenerator;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.AuthorFormDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
	AuthorRestController.class,
	RestControllerErrorHandler.class,
	MethodArgumentNotValidExceptionToValidationErrorDtoConverter.class
})
@WithMockUser(roles = {"USER", "AUTHOR_MANAGER"})
public class AuthorRestControllerTest extends AbstractControllerTest {

	@MockBean
	private AuthorService authorService;

	@Test
	void whenGetAuthorsThenSuccess() throws Exception {
		List<AuthorDto> authors = AuthorDtoGenerator.generateList();

		when(authorService.findAll()).thenReturn(authors);

		mockMvc.perform(get("/api/v1/authors"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(authors.size())))
			.andExpect(jsonPath("$.[0].id", is(Long.valueOf(authors.get(0).getId()).intValue())))
			.andExpect(jsonPath("$.[0].fullName", is(authors.get(0).getFullName())))
			.andExpect(jsonPath("$.[1].id", is(Long.valueOf(authors.get(1).getId()).intValue())))
			.andExpect(jsonPath("$.[1].fullName", is(authors.get(1).getFullName())));

		verify(authorService).findAll();
	}

	@Test
	void whenGetAuthorsAndAuthorsAreEmptyThenSuccess() throws Exception {
		when(authorService.findAll()).thenReturn(List.of());

		mockMvc.perform(get("/api/v1/authors"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));

		verify(authorService).findAll();
	}

	@Test
	void whenGetAuthorThenSuccess() throws Exception {
		AuthorDto author = AuthorDtoGenerator.generate();

		when(authorService.findById(author.getId())).thenReturn(author);

		mockMvc.perform(get("/api/v1/authors/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(Long.valueOf(author.getId()).intValue())))
			.andExpect(jsonPath("$.fullName", is(author.getFullName())));

		verify(authorService).findById(author.getId());
	}

	@Test
	void whenAddAuthorThenSuccess() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generateNewAuthor();

		mockMvc.perform(post("/api/v1/authors")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(author))
				.with(csrf()))
			.andExpect(status().isOk());

		verify(authorService).save(author);
	}

	@Test
	void whenAddAuthorAndFullNameEmptyThenBadRequest() throws Exception {
		AuthorFormDto authorFormDto = AuthorFormDtoGenerator.generateNewWithEmptyFullName();

		mockMvc.perform(post("/api/v1/authors")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(authorFormDto))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fields", hasSize(1)))
			.andExpect(jsonPath("$.fields[0].fieldName", is("fullName")))
			.andExpect(jsonPath("$.fields[0].error", is("must not be blank")));

		verify(authorService, never()).save(any());
	}

	@Test
	void whenAddAuthorAndFullNameExistsThenBadRequest() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generateNewAuthor();

		doThrow(FullNameDuplicateException.byFullName(author.getFullName())).when(authorService).save(author);

		mockMvc.perform(post("/api/v1/authors")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(author))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message", is("Author with same fullName name_1 already exists")));

		verify(authorService).save(author);
	}

	@Test
	void whenEditAuthorThenSuccess() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generate();

		mockMvc.perform(put("/api/v1/authors/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(author))
				.with(csrf()))
			.andExpect(status().isOk());

		verify(authorService).save(author);
	}

	@Test
	void whenEditAuthorAndFullNameEmptyBadRequest() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generateWithEmptyFullName();

		mockMvc.perform(put("/api/v1/authors/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(author))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fields", hasSize(1)))
			.andExpect(jsonPath("$.fields[0].fieldName", is("fullName")))
			.andExpect(jsonPath("$.fields[0].error", is("must not be blank")));

		verify(authorService, never()).save(any());
	}

	@Test
	void whenEditAuthorAndFullNameExistsThenBadRequest() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generate();

		doThrow(FullNameDuplicateException.byFullName(author.getFullName())).when(authorService).save(author);

		mockMvc.perform(put("/api/v1/authors/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(author))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message", is("Author with same fullName name_1 already exists")));

		verify(authorService).save(author);
	}

	@Test
	void whenDeleteAuthorThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();

		mockMvc.perform(delete("/api/v1/authors/1")
				.with(csrf()))
			.andExpect(status().isOk());

		verify(authorService).deleteById(author.getId());
	}

	@Test
	void whenThereAreBookDependsOnDeleteAuthorThenBadRequest() throws Exception {
		AuthorDto author = AuthorDtoGenerator.generate();

		doThrow(DeleteEntityException.authorByBooksDependency(author.getId())).when(authorService).deleteById(author.getId());

		mockMvc.perform(delete("/api/v1/authors/1")
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message", is("There are books by author with id 1")));

		verify(authorService).deleteById(author.getId());
	}
}
