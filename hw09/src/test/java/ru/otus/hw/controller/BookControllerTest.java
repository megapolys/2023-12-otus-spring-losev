package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.generator.BookDtoGenerator;
import ru.otus.hw.generator.BookFormDtoGenerator;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookFormDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = BookController.class)
class BookControllerTest extends AbstractControllerTest {

	@MockBean
	private BookService bookService;

	@MockBean
	private AuthorService authorService;

	@MockBean
	private GenreService genreService;

	@Test
	void whenReturnBooksThenSuccess() throws Exception {
		List<BookDto> books = BookDtoGenerator.generateList();

		when(bookService.findAll()).thenReturn(books);

		mockMvc.perform(get("/books"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/list"))
			.andExpect(model().attributeExists("books"))
			.andExpect(model().attribute("books", equalTo(books)));

		verify(bookService).findAll();
	}

	@Test
	void whenReturnEmptyBooksThenSuccess() throws Exception {
		when(bookService.findAll()).thenReturn(List.of());

		mockMvc.perform(get("/books"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/list"))
			.andExpect(model().attributeExists("books"))
			.andExpect(model().attribute("books", equalTo(List.of())));

		verify(bookService).findAll();
	}

	@Test
	void whenGetEditBookThenSuccess() throws Exception {
		BookDto book = BookDtoGenerator.generate();

		when(bookService.findById(book.getId())).thenReturn(book);

		mockMvc.perform(get("/books/edit/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/edit"))
			.andExpect(model().attributeExists("book"))
			.andExpect(model().attribute("book", equalTo(book)));

		verify(bookService).findById(book.getId());
	}

	@Test
	void whenEditBookThenSuccess() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		mockMvc.perform(post("/books/edit/{id}", String.valueOf(book.getId()))
				.param("title", book.getTitle())
				.param("authorId", String.valueOf(book.getAuthorId()))
				.param("genreIds", "1", "2")
			).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books"));

		verify(bookService).save(book);
	}

	@Test
	void whenEditBookWithExceptionThenSuccess() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generateWithEmptyTitle();

		doThrow(new RuntimeException("exception")).when(bookService).save(book);

		mockMvc.perform(post("/books/edit/{id}", String.valueOf(book.getId()))
				.param("title", book.getTitle())
				.param("authorId", String.valueOf(book.getAuthorId()))
				.param("genreIds", "1", "2")
			).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books/edit/{id}"))
			.andExpect(flash().attribute("errorMessage", equalTo("exception")));

		verify(bookService).save(book);
	}

	@Test
	void whenGetAddBookThenSuccess() throws Exception {
		mockMvc.perform(get("/books/add"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/add"))
			.andExpect(model().attributeExists("genres", "authors"));
	}

	@Test
	void whenAddBookThenSuccess() throws Exception {
		BookFormDto book = BookFormDtoGenerator.generate();

		doThrow(new RuntimeException("exception")).when(bookService).save(book);

		mockMvc.perform(post("/books/add")
				.param("title", book.getTitle())
				.param("authorId", String.valueOf(book.getAuthorId()))
				.param("genreIds", "1", "2")
			).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books"));

		verify(bookService).save(any());
	}

	@Test
	void whenDeleteBookThenSuccess() throws Exception {
		mockMvc.perform(post("/books/delete/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books"));

		verify(bookService).deleteById(1L);
	}

}