package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookFormDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

	private final BookService bookService;

	@GetMapping("api/v1/books")
	public List<BookDto> getBooks() {
		return bookService.findAll();
	}

	@GetMapping(value = "api/v1/books/{id}")
	public BookDto getBookById(@PathVariable long id) {
		return bookService.findById(id);
	}

	@PostMapping(value = "api/v1/books")
	public void addBook(@RequestBody @Valid BookFormDto book) {
		bookService.save(book);
	}

	@PutMapping(value = "api/v1/books/{id}")
	public void editBook(
		@PathVariable long id,
		@RequestBody @Valid BookFormDto book
	) {
		book.setId(id);
		bookService.save(book);
	}

	@DeleteMapping(value = "api/v1/books/{id}")
	public void deleteBook(@PathVariable("id") long id) {
		bookService.deleteById(id);
	}
}
