package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.generator.AuthorDtoGenerator;
import ru.otus.hw.generator.AuthorGenerator;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorControllerTest extends AbstractControllerTest {

	@MockBean
	private AuthorRepository authorRepository;

	@MockBean
	private BookRepository bookRepository;

	@Test
	void whenReturnAuthorsThenSuccess() throws Exception {
		List<Author> authors = AuthorGenerator.generateList();
		List<AuthorDto> expectedAuthors = AuthorDtoGenerator.generateList();

		when(authorRepository.findAll()).thenReturn(authors);

		mockMvc.perform(get("/authors"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/list"))
			.andExpect(model().attributeExists("authors"))
			.andExpect(model().attribute("authors", equalTo(expectedAuthors)));

		verify(authorRepository).findAll();
	}

	@Test
	void whenReturnEmptyAuthorsThenSuccess() throws Exception {
		when(authorRepository.findAll()).thenReturn(List.of());

		mockMvc.perform(get("/authors"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/list"))
			.andExpect(model().attributeExists("authors"))
			.andExpect(model().attribute("authors", equalTo(List.of())));

		verify(authorRepository).findAll();
	}

	@Test
	void whenGetEditAuthorThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();
		AuthorDto expectedAuthor = AuthorDtoGenerator.generate();

		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

		mockMvc.perform(get("/authors/edit")
				.param("id", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/edit"))
			.andExpect(model().attributeExists("author"))
			.andExpect(model().attribute("author", equalTo(expectedAuthor)));

		verify(authorRepository).findById(author.getId());
	}

	@Test
	void whenEditAuthorThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();

		when(authorRepository.existsByFullNameAndIdNot(author.getFullName(), author.getId())).thenReturn(false);

		mockMvc.perform(post("/authors/edit")
				.param("id", "1")
				.param("fullName", "name_1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/authors"));

		verify(authorRepository).save(author);
	}

	@Test
	void whenEditAuthorAndFullNameEmptyThenSuccess() throws Exception {
		AuthorDto authorDto = AuthorDtoGenerator.generateWithEmptyFullName();

		mockMvc.perform(post("/authors/edit")
				.param("id", "1")
				.param("fullName", ""))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/edit"))
			.andExpect(model().attributeExists("author"))
			.andExpect(model().attribute("author", equalTo(authorDto)))
			.andExpect(model().errorCount(1))
			.andExpect(model().attributeHasFieldErrorCode("author", "fullName", "NotBlank"));

		verify(authorRepository, never()).existsByFullNameAndIdNot(any(), anyLong());
		verify(authorRepository, never()).save(any());
	}

	@Test
	void whenEditAuthorAndFullNameExistsThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();
		AuthorDto authorDto = AuthorDtoGenerator.generate();

		when(authorRepository.existsByFullNameAndIdNot(author.getFullName(), author.getId())).thenReturn(true);

		mockMvc.perform(post("/authors/edit")
				.param("id", "1")
				.param("fullName", "name_1"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/edit"))
			.andExpect(model().attributeExists("author", "errorMessage"))
			.andExpect(model().attribute("author", equalTo(authorDto)))
			.andExpect(model().attribute("errorMessage", equalTo("Author with same fullName name_1 already exists")));

		verify(authorRepository).existsByFullNameAndIdNot(author.getFullName(), author.getId());
		verify(authorRepository, never()).save(any());
	}

	@Test
	void whenGetAddAuthorThenSuccess() throws Exception {
		mockMvc.perform(get("/authors/add"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/add"))
			.andExpect(model().attributeDoesNotExist("author"));

		verify(authorRepository, never()).findById(any());
	}

	@Test
	void whenAddAuthorThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();

		when(authorRepository.existsByFullNameAndIdNot(author.getFullName(), author.getId())).thenReturn(false);

		mockMvc.perform(post("/authors/add")
				.param("id", "1")
				.param("fullName", "name_1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/authors"));

		verify(authorRepository).save(author);
	}

	@Test
	void whenAddAuthorAndFullNameEmptyThenSuccess() throws Exception {
		AuthorDto authorDto = AuthorDtoGenerator.generateWithEmptyFullName();

		mockMvc.perform(post("/authors/add")
				.param("id", "1")
				.param("fullName", ""))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/add"))
			.andExpect(model().attributeExists("author"))
			.andExpect(model().attribute("author", equalTo(authorDto)))
			.andExpect(model().errorCount(1))
			.andExpect(model().attributeHasFieldErrorCode("author", "fullName", "NotBlank"));

		verify(authorRepository, never()).existsByFullNameAndIdNot(any(), anyLong());
		verify(authorRepository, never()).save(any());
	}

	@Test
	void whenAddAuthorAndFullNameExistsThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();
		AuthorDto authorDto = AuthorDtoGenerator.generate();

		when(authorRepository.existsByFullNameAndIdNot(author.getFullName(), author.getId())).thenReturn(true);

		mockMvc.perform(post("/authors/add")
				.param("id", "1")
				.param("fullName", "name_1"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/add"))
			.andExpect(model().attributeExists("author", "errorMessage"))
			.andExpect(model().attribute("author", equalTo(authorDto)))
			.andExpect(model().attribute("errorMessage", equalTo("Author with same fullName name_1 already exists")));

		verify(authorRepository).existsByFullNameAndIdNot(author.getFullName(), author.getId());
		verify(authorRepository, never()).save(any());
	}

	@Test
	void whenDeleteAuthorThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();

		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
		when(bookRepository.existsBookByAuthor(author)).thenReturn(false);

		mockMvc.perform(post("/authors/delete")
			.param("id", "1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/authors"));

		verify(authorRepository).deleteById(author.getId());
	}

	@Test
	void whenThereAreBookDependsOnDeleteAuthorThenSuccess() throws Exception {
		Author author = AuthorGenerator.generate();

		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
		when(bookRepository.existsBookByAuthor(author)).thenReturn(true);

		mockMvc.perform(post("/authors/delete")
			.param("id", "1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/authors"))
			.andExpect(flash().attribute("errorMessage", "There are books by author with id 1"));

		verify(authorRepository, never()).deleteById(any());
	}

}