package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.generator.AuthorGenerator;
import ru.otus.hw.generator.BookDtoGenerator;
import ru.otus.hw.generator.BookFormDtoGenerator;
import ru.otus.hw.generator.BookGenerator;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookFormDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest extends AbstractControllerTest {

	@MockBean
	private BookRepository bookRepository;

	@MockBean
	private AuthorRepository authorRepository;

	@MockBean
	private GenreRepository genreRepository;

	@Test
	void whenReturnBooksThenSuccess() throws Exception {
		List<Book> books = BookGenerator.generateList();
		List<BookDto> expectedBooks = BookDtoGenerator.generateList();

		when(bookRepository.findAll()).thenReturn(books);

		mockMvc.perform(get("/books"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/list"))
			.andExpect(model().attributeExists("books"))
			.andExpect(model().attribute("books", equalTo(expectedBooks)));

		verify(bookRepository).findAll();
	}

	@Test
	void whenReturnEmptyBooksThenSuccess() throws Exception {
		when(bookRepository.findAll()).thenReturn(List.of());

		mockMvc.perform(get("/books"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/list"))
			.andExpect(model().attributeExists("books"))
			.andExpect(model().attribute("books", equalTo(List.of())));

		verify(bookRepository).findAll();
	}

	@Test
	void whenGetEditBookThenSuccess() throws Exception {
		Book book = BookGenerator.generate();
		BookDto expectedBook = BookDtoGenerator.generate();

		when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

		mockMvc.perform(get("/books/edit")
				.param("id", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/edit"))
			.andExpect(model().attributeExists("book"))
			.andExpect(model().attribute("book", equalTo(expectedBook)));

		verify(bookRepository).findById(book.getId());
	}

	@Test
	void whenEditBookThenSuccess() throws Exception {
		Book book = BookGenerator.generate();
		BookFormDto bookFormDto = BookFormDtoGenerator.generate();

		when(genreRepository.findAllByIds(bookFormDto.getGenreIds())).thenReturn(book.getGenres());
		when(authorRepository.findById(bookFormDto.getAuthorId())).thenReturn(Optional.of(book.getAuthor()));
		when(bookRepository.save(any())).thenReturn(book);

		mockMvc.perform(post("/books/edit")
				.param("id", String.valueOf(bookFormDto.getId()))
				.param("title", bookFormDto.getTitle())
				.param("authorId", String.valueOf(bookFormDto.getAuthorId()))
				.param("genreIds", "1", "2")
			).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books"));

		verify(bookRepository).save(any());
	}

	@Test
	void whenEditBookWithEmptyTitleThenSuccess() throws Exception {
		BookFormDto bookFormDto = BookFormDtoGenerator.generateWithEmptyTitle();

		mockMvc.perform(post("/books/edit")
				.param("id", String.valueOf(bookFormDto.getId()))
				.param("title", bookFormDto.getTitle())
				.param("authorId", String.valueOf(bookFormDto.getAuthorId()))
				.param("genreIds", "1", "2")
			).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books/edit?id={id}"))
			.andExpect(flash().attribute("errorMessage", equalTo("Title must be not empty")));

		verify(bookRepository, never()).save(any());
	}

	@Test
	void whenEditBookWithEmptyGenresThenSuccess() throws Exception {
		BookFormDto bookFormDto = BookFormDtoGenerator.generate();

		mockMvc.perform(post("/books/edit")
				.param("id", String.valueOf(bookFormDto.getId()))
				.param("title", bookFormDto.getTitle())
				.param("authorId", String.valueOf(bookFormDto.getAuthorId()))
			).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books/edit?id={id}"))
			.andExpect(flash().attribute("errorMessage", equalTo("Genres ids must not be null")));

		verify(bookRepository, never()).save(any());
	}

	@Test
	void whenEditBookWithAuthorNotFoundThenSuccess() throws Exception {
		Book book = BookGenerator.generate();
		BookFormDto bookFormDto = BookFormDtoGenerator.generate();

		when(genreRepository.findAllByIds(bookFormDto.getGenreIds())).thenReturn(book.getGenres());
		when(authorRepository.findById(bookFormDto.getAuthorId())).thenReturn(Optional.empty());

		mockMvc.perform(post("/books/edit")
				.param("id", String.valueOf(bookFormDto.getId()))
				.param("title", bookFormDto.getTitle())
				.param("authorId", String.valueOf(bookFormDto.getAuthorId()))
				.param("genreIds", "1", "2")
			).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books/edit?id={id}"))
			.andExpect(flash().attribute("errorMessage", equalTo("Author with id 1 not found")));

		verify(bookRepository, never()).save(any());
	}

	@Test
	void whenEditBookWithGenresNotFoundThenSuccess() throws Exception {
		BookFormDto bookFormDto = BookFormDtoGenerator.generate();

		when(genreRepository.findAllByIds(bookFormDto.getGenreIds())).thenReturn(List.of());

		mockMvc.perform(post("/books/edit")
				.param("id", String.valueOf(bookFormDto.getId()))
				.param("title", bookFormDto.getTitle())
				.param("authorId", String.valueOf(bookFormDto.getAuthorId()))
				.param("genreIds", "1", "2")
			).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books/edit?id={id}"))
			.andExpect(flash().attribute("errorMessage", equalTo("One or all genres with ids [1, 2] not found")));

		verify(bookRepository, never()).save(any());
	}

	@Test
	void whenDeleteBookThenSuccess() throws Exception {
		mockMvc.perform(post("/books/delete")
				.param("id", "1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books"));

		verify(bookRepository).deleteById(1L);
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
		Book book = BookGenerator.generate();
		BookFormDto bookFormDto = BookFormDtoGenerator.generate();

		when(genreRepository.findAllByIds(bookFormDto.getGenreIds())).thenReturn(book.getGenres());
		when(authorRepository.findById(bookFormDto.getAuthorId())).thenReturn(Optional.of(book.getAuthor()));
		when(bookRepository.save(any())).thenReturn(book);

		mockMvc.perform(post("/books/add")
				.param("title", bookFormDto.getTitle())
				.param("authorId", String.valueOf(bookFormDto.getAuthorId()))
				.param("genreIds", "1", "2")
			).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books"));

		verify(bookRepository).save(any());
	}

}