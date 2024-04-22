package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.generator.BookDtoGenerator;
import ru.otus.hw.generator.BookFormDtoGenerator;
import ru.otus.hw.generator.BookGenerator;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookFormDto;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest extends AbstractServiceTest {
	
	@Autowired
	private BookService bookService;

	@Test
	void whenFindAllThenReturn() {
		List<Book> books = BookGenerator.generateList();
		List<BookDto> expectedBooks = BookDtoGenerator.generateList();

		when(bookRepository.findAll()).thenReturn(books);

		List<BookDto> actual = bookService.findAll();

		then(actual).isEqualTo(expectedBooks);

		verify(bookRepository).findAll();
	}

	@Test
	void whenFindAllThenReturnEmptyList() {
		when(bookRepository.findAll()).thenReturn(List.of());

		List<BookDto> actual = bookService.findAll();

		then(actual).isEmpty();

		verify(bookRepository).findAll();
	}

	@Test
	void whenFindByIdThenReturnBook() {
		Book book = BookGenerator.generate();
		BookDto expectedBook = BookDtoGenerator.generate();

		when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(book));

		BookDto actual = bookService.findById(book.getId());

		then(actual).isEqualTo(expectedBook);

		verify(bookRepository).findById(expectedBook.getId());
	}

	@Test
	void whenSaveBookThenSave() {
		Book book = BookGenerator.generate();
		BookFormDto bookFormDto = BookFormDtoGenerator.generate();

		when(genreRepository.findAllByIds(bookFormDto.getGenreIds())).thenReturn(book.getGenres());
		when(authorRepository.findById(bookFormDto.getAuthorId())).thenReturn(Optional.of(book.getAuthor()));
		when(bookRepository.save(any())).thenReturn(book);

		bookService.save(bookFormDto);

		verify(bookRepository).save(any());
	}

	@Test
	void whenSaveBookThenUpdate() {
		Book book = BookGenerator.generate();
		BookFormDto bookFormDto = BookFormDtoGenerator.generate();

		when(genreRepository.findAllByIds(bookFormDto.getGenreIds())).thenReturn(book.getGenres());
		when(authorRepository.findById(bookFormDto.getAuthorId())).thenReturn(Optional.of(book.getAuthor()));
		when(bookRepository.save(any())).thenReturn(book);

		bookService.save(bookFormDto);

		verify(bookRepository).save(any());
	}

	@Test
	void whenSaveBookWithAuthorNotFoundThenException() {
		Book book = BookGenerator.generate();
		BookFormDto bookFormDto = BookFormDtoGenerator.generate();

		when(genreRepository.findAllByIds(bookFormDto.getGenreIds())).thenReturn(book.getGenres());
		when(authorRepository.findById(bookFormDto.getAuthorId())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			bookService.save(bookFormDto);
		}).isInstanceOf(EntityNotFoundException.class)
			.hasMessage("Author with id 1 not found");

		verify(bookRepository, never()).save(any());
	}

	@Test
	void whenSaveBookWithGenresNotFoundThenException() {
		BookFormDto bookFormDto = BookFormDtoGenerator.generate();

		when(genreRepository.findAllByIds(bookFormDto.getGenreIds())).thenReturn(List.of());

		assertThatThrownBy(() -> {
			bookService.save(bookFormDto);
		}).isInstanceOf(EntityNotFoundException.class)
			.hasMessage("One or all genres with ids [1, 2] not found");

		verify(bookRepository, never()).save(any());
	}

	@Test
	void whenDeleteBookThenSuccess() {
		bookService.deleteById(1L);

		verify(bookRepository).deleteById(1L);
	}
}
