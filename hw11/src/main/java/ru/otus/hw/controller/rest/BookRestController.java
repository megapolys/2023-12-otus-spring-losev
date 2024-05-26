package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookFormDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

	private final BookRepository bookRepository;

	private final AuthorRepository authorRepository;

	private final GenreRepository genreRepository;

	@GetMapping("api/v1/books")
	public Flux<BookDto> getBooks() {
		return bookRepository.findAll()
			.map(BookDto::new);
	}

	@GetMapping(value = "api/v1/books/{id}")
	public Mono<ResponseEntity<BookDto>> getBookById(@PathVariable String id) {
		return bookRepository.findById(id)
			.map(BookDto::new)
			.map(ResponseEntity::ok)
			.switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
	}

	@PostMapping(value = "api/v1/books")
	public Mono<BookDto> addBook(@RequestBody @Valid BookFormDto book) {
		return save(book);
	}

	@PutMapping(value = "api/v1/books/{id}")
	public Mono<BookDto> editBook(
		@PathVariable String id,
		@RequestBody @Valid BookFormDto book
	) {
		book.setId(id);
		return save(book);
	}

	@DeleteMapping(value = "api/v1/books/{id}")
	public Mono<Void> deleteBook(@PathVariable("id") String id) {
		return bookRepository.deleteById(id);
	}

	private Mono<BookDto> save(BookFormDto bookFormDto) {
		return Mono.zip(getAuthor(bookFormDto), getGenres(bookFormDto))
			.flatMap(tuple2 -> bookRepository.save(
				new Book(
					bookFormDto.getId(),
					bookFormDto.getTitle(),
					tuple2.getT1(),
					tuple2.getT2())
			)).map(BookDto::new);
	}

	private Mono<Author> getAuthor(BookFormDto bookFormDto) {
		return Mono.just(bookFormDto)
			.flatMap(book -> authorRepository.findById(book.getAuthorId()))
			.switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %s not found"
				.formatted(bookFormDto.getAuthorId()))));
	}

	private Mono<List<Genre>> getGenres(BookFormDto bookFormDto) {
		return genreRepository.findAllByIds(bookFormDto.getGenreIds()).collectList();
	}
}
