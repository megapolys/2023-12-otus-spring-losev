package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.controller.AbstractControllerTest;
import ru.otus.hw.controller.advice.RestControllerErrorHandler;
import ru.otus.hw.converters.MethodArgumentNotValidExceptionToValidationErrorDtoConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.generator.BookDtoGenerator;
import ru.otus.hw.generator.BookFormDtoGenerator;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookFormDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
	BookRestController.class,
	RestControllerErrorHandler.class,
	MethodArgumentNotValidExceptionToValidationErrorDtoConverter.class
})
@WithMockUser
public class BookRestControllerTest extends AbstractControllerTest {

	@MockBean
	private BookService bookService;

	@Test
	void whenReturnBooksThenSuccess() throws Exception {
		List<BookDto> books = BookDtoGenerator.generateList();

		when(bookService.findAll()).thenReturn(books);

		mockMvc.perform(get("/api/v1/books"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(books.size())))
			.andExpect(jsonPath("$.[0].id", is(Long.valueOf(books.get(0).getId()).intValue())))
			.andExpect(jsonPath("$.[0].title", is(books.get(0).getTitle())))
			.andExpect(jsonPath("$.[0].author.id", is(Long.valueOf(books.get(0).getAuthor().getId()).intValue())))
			.andExpect(jsonPath("$.[0].author.fullName", is(books.get(0).getAuthor().getFullName())))
			.andExpect(jsonPath("$.[0].genres", hasSize(books.get(0).getGenres().size())))
			.andExpect(jsonPath("$.[0].genres[0].id", is(Long.valueOf(books.get(0).getGenres().get(0).getId()).intValue())))
			.andExpect(jsonPath("$.[0].genres[0].name", is(books.get(0).getGenres().get(0).getName())))
			.andExpect(jsonPath("$.[1].id", is(Long.valueOf(books.get(1).getId()).intValue())))
			.andExpect(jsonPath("$.[1].title", is(books.get(1).getTitle())))
			.andExpect(jsonPath("$.[1].author.id", is(Long.valueOf(books.get(1).getAuthor().getId()).intValue())))
			.andExpect(jsonPath("$.[1].author.fullName", is(books.get(1).getAuthor().getFullName())))
			.andExpect(jsonPath("$.[1].genres", hasSize(books.get(1).getGenres().size())))
			.andExpect(jsonPath("$.[1].genres[0].id", is(Long.valueOf(books.get(1).getGenres().get(0).getId()).intValue())))
			.andExpect(jsonPath("$.[1].genres[0].name", is(books.get(1).getGenres().get(0).getName())))
			.andExpect(jsonPath("$.[1].genres[1].id", is(Long.valueOf(books.get(1).getGenres().get(1).getId()).intValue())))
			.andExpect(jsonPath("$.[1].genres[1].name", is(books.get(1).getGenres().get(1).getName())));

		verify(bookService).findAll();
	}

	@Test
	void whenReturnEmptyBooksThenSuccess() throws Exception {
		when(bookService.findAll()).thenReturn(List.of());

		mockMvc.perform(get("/api/v1/books"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));

		verify(bookService).findAll();
	}

	@Test
	void whenReturnBookThenSuccess() throws Exception {
		BookDto book = BookDtoGenerator.generate();

		when(bookService.findById(book.getId())).thenReturn(book);

		mockMvc.perform(get("/api/v1/books/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(Long.valueOf(book.getId()).intValue())))
			.andExpect(jsonPath("$.title", is(book.getTitle())))
			.andExpect(jsonPath("$.author.id", is(Long.valueOf(book.getAuthor().getId()).intValue())))
			.andExpect(jsonPath("$.author.fullName", is(book.getAuthor().getFullName())))
			.andExpect(jsonPath("$.genres", hasSize(book.getGenres().size())))
			.andExpect(jsonPath("$.genres[0].id", is(Long.valueOf(book.getGenres().get(0).getId()).intValue())))
			.andExpect(jsonPath("$.genres[0].name", is(book.getGenres().get(0).getName())))
			.andExpect(jsonPath("$.genres[1].id", is(Long.valueOf(book.getGenres().get(1).getId()).intValue())))
			.andExpect(jsonPath("$.genres[1].name", is(book.getGenres().get(1).getName())));

		verify(bookService).findById(book.getId());
	}

	@Test
	void whenAddBookThenSuccess() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		mockMvc.perform(post("/api/v1/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book))
				.with(csrf())
			).andExpect(status().isOk());

		verify(bookService).save(book);
	}

	@Test
	void whenEditBookThenSuccess() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		mockMvc.perform(put("/api/v1/books/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(book))
			.with(csrf())
		).andExpect(status().isOk());

		verify(bookService).save(book);
	}

	@Test
	void whenEditBookWithInvalidFieldsThenBadRequest() throws Exception {
		BookFormDto bookFormDto = BookFormDtoGenerator.generateInvalid();

		Map<String, String> titleError = Map.of(
			"fieldName", "title",
			"error", "must not be blank"
		);
		Map<String, String> genresError = Map.of(
			"fieldName", "genreIds",
			"error", "must not be empty"
		);
		mockMvc.perform(put("/api/v1/books/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookFormDto))
				.with(csrf())
			).andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fields", hasSize(2)))
			.andExpect(jsonPath("$.fields", hasItem(titleError)))
			.andExpect(jsonPath("$.fields", hasItem(genresError)));

		verify(bookService, never()).save(any());
	}

	@Test
	void whenEditBookWithAuthorNotFoundThenBadRequest() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		doThrow(new EntityNotFoundException("Author with id 1 not found"))
			.when(bookService).save(book);

		mockMvc.perform(put("/api/v1/books/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book))
				.with(csrf())
			).andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message", is("Author with id 1 not found")));

		verify(bookService).save(book);
	}

	@Test
	void whenEditBookWithGenresNotFoundThenSuccess() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		doThrow(new EntityNotFoundException("One or all genres with ids [1, 2] not found"))
			.when(bookService).save(book);

		mockMvc.perform(put("/api/v1/books/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book))
				.with(csrf())
			).andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message", is("One or all genres with ids [1, 2] not found")));

		verify(bookService).save(book);
	}

	@Test
	void whenDeleteBookThenSuccess() throws Exception {
		mockMvc.perform(delete("/api/v1/books/1")
				.with(csrf())
			).andExpect(status().isOk());

		verify(bookService).deleteById(1L);
	}
}
