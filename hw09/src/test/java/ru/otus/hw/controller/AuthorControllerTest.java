package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.generator.AuthorDtoGenerator;
import ru.otus.hw.generator.AuthorFormDtoGenerator;
import ru.otus.hw.generator.AuthorGenerator;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.AuthorFormDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = AuthorController.class)
class AuthorControllerTest extends AbstractControllerTest {

	@MockBean
	private AuthorService authorService;

	@Test
	void whenReturnAuthorsThenSuccess() throws Exception {
		List<AuthorDto> authors = AuthorDtoGenerator.generateList();

		when(authorService.findAll()).thenReturn(authors);

		mockMvc.perform(get("/authors"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/list"))
			.andExpect(model().attributeExists("authors"))
			.andExpect(model().attribute("authors", equalTo(authors)));

		verify(authorService).findAll();
	}

	@Test
	void whenReturnEmptyAuthorsThenSuccess() throws Exception {
		when(authorService.findAll()).thenReturn(List.of());

		mockMvc.perform(get("/authors"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/list"))
			.andExpect(model().attributeExists("authors"))
			.andExpect(model().attribute("authors", equalTo(List.of())));

		verify(authorService).findAll();
	}

	@Test
	void whenGetEditAuthorThenSuccess() throws Exception {
		AuthorDto author = AuthorDtoGenerator.generate();

		when(authorService.findById(author.getId())).thenReturn(author);

		mockMvc.perform(get("/authors/edit/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/edit"))
			.andExpect(model().attributeExists("author"))
			.andExpect(model().attribute("author", equalTo(author)));

		verify(authorService).findById(author.getId());
	}

	@Test
	void whenEditAuthorThenSuccess() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generate();

		mockMvc.perform(post("/authors/edit")
				.param("id", "1")
				.param("fullName", "name_1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/authors"));

		verify(authorService).save(author);
	}

	@Test
	void whenEditAuthorWithExceptionThenSuccess() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generate();

		doThrow(new RuntimeException("exception")).when(authorService).save(author);

		mockMvc.perform(post("/authors/edit")
				.param("id", "1")
				.param("fullName", "name_1"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/edit"))
			.andExpect(model().attributeExists("author", "errorMessage"))
			.andExpect(model().attribute("author", equalTo(author)))
			.andExpect(model().attribute("errorMessage", equalTo("exception")));

		verify(authorService).save(author);
	}

	@Test
	void whenGetAddAuthorThenSuccess() throws Exception {
		mockMvc.perform(get("/authors/add"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/add"))
			.andExpect(model().attributeDoesNotExist("author"));
	}

	@Test
	void whenAddAuthorThenSuccess() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generate();

		mockMvc.perform(post("/authors/add")
				.param("id", "1")
				.param("fullName", "name_1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/authors"));

		verify(authorService).save(author);
	}

	@Test
	void whenAddAuthorAndFullNameEmptyThenSuccess() throws Exception {
		AuthorFormDto author = AuthorFormDtoGenerator.generateWithEmptyFullName();

		doThrow(new RuntimeException("exception")).when(authorService).save(author);

		mockMvc.perform(post("/authors/add")
				.param("id", "1")
				.param("fullName", ""))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/add"))
			.andExpect(model().attributeExists("author"))
			.andExpect(model().attribute("author", equalTo(author)))
			.andExpect(model().attribute("errorMessage", equalTo("exception")));

		verify(authorService).save(author);
	}

	@Test
	void whenDeleteAuthorThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();

		mockMvc.perform(post("/authors/delete/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/authors"));

		verify(authorService).deleteById(author.getId());
	}

	@Test
	void whenThereAreBookDependsOnDeleteAuthorThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();

		doThrow(new RuntimeException("exception")).when(authorService).deleteById(author.getId());

		mockMvc.perform(post("/authors/delete/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/authors"))
			.andExpect(flash().attribute("errorMessage", "exception"));

		verify(authorService).deleteById(author.getId());
	}
}