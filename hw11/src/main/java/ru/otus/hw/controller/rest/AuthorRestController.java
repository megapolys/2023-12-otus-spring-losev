package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
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
import ru.otus.hw.exceptions.DeleteEntityException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.author.FullNameDuplicateException;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.AuthorFormDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {

	private final AuthorRepository authorRepository;

	private final BookRepository bookRepository;

	private final ConversionService conversionService;

	@GetMapping("api/v1/authors")
	public Flux<AuthorDto> getAuthors() {
		return authorRepository.findAll()
			.map(AuthorDto::new);
	}

	@GetMapping(value = "api/v1/authors/{id}")
	public Mono<ResponseEntity<AuthorDto>> getAuthorById(@PathVariable String id) {
		return authorRepository.findById(id)
			.map(AuthorDto::new)
			.map(ResponseEntity::ok)
			.switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
	}

	@PostMapping(value = "api/v1/authors")
	public Mono<AuthorDto> addAuthor(@RequestBody @Valid AuthorFormDto author) {
		return save(author);
	}

	@PutMapping(value = "api/v1/authors/{id}")
	public Mono<AuthorDto> updateAuthor(
		@PathVariable String id,
		@RequestBody @Valid AuthorFormDto author
	) {
		author.setId(id);
		return save(author);
	}

	@DeleteMapping(value = "api/v1/authors/{id}")
	public Mono<Void> deleteAuthor(@PathVariable("id") String id) {
		return authorRepository.findById(id)
			.flatMap(author -> {
				if (author == null) {
					return Mono.error(new EntityNotFoundException("Author with id %s not found."
						.formatted(id)));
				} else {
					return deleteAuthor(id, author);
				}
			});
	}

	private Mono<AuthorDto> save(AuthorFormDto author) {
		return authorRepository.existsByFullNameAndIdNot(author.getFullName(), author.getId())
			.flatMap(existAuthor -> {
				if (existAuthor) {
					return Mono.error(FullNameDuplicateException.byFullName(author.getFullName()));
				} else {
					return authorRepository.save(Objects.requireNonNull(
						conversionService.convert(author, Author.class)));
				}
			})
			.map(AuthorDto::new);
	}

	private Mono<Void> deleteAuthor(String id, Author author) {
		return bookRepository.existsBookByAuthor(author)
			.flatMap(bookExist -> {
				if (bookExist) {
					return Mono.error(DeleteEntityException
						.authorByBooksDependency(id));
				}
				return authorRepository.deleteById(id);
			});
	}
}
