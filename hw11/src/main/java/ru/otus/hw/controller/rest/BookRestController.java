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

import java.util.Set;
import java.util.stream.Collectors;

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
		Author author = getAuthor(bookFormDto);
		Set<Genre> genres = getGenres(bookFormDto);
		Book book = new Book(bookFormDto.getId(), bookFormDto.getTitle(), author, genres);
		return bookRepository.save(book)
			.map(BookDto::new);
	}

	private Author getAuthor(BookFormDto bookFormDto) {
		return authorRepository.findById(bookFormDto.getAuthorId())
			.map(author -> {
				if (author == null) {
					throw new EntityNotFoundException("One or all genres with ids %s not found"
						.formatted(bookFormDto.getGenreIds()));
				} else {
					return author;
				}
			}).block();
	}

	private Set<Genre> getGenres(BookFormDto bookFormDto) {
		return Flux.fromStream(bookFormDto.getGenreIds().stream())
			.map(genreRepository::findById)
			.flatMap(genre -> {
				if (genre == null) {
					return Flux.error(new EntityNotFoundException(
						"One or all genres with ids %s not found"
							.formatted(bookFormDto.getGenreIds())));
				} else {
					return genre;
				}
			}).collect(Collectors.toSet())
			.block();
	}
}
