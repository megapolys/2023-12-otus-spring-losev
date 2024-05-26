package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
			.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
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
	public Mono<Void> deleteAuthor(@PathVariable("id") String authorId) {
		return Mono.just(authorId)
			.filterWhen(authorRepository::existsById)
			.switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %s not found."
				.formatted(authorId))))
			.filterWhen(id -> bookRepository.existsBookByAuthorId(id)
				.map(exist -> !exist))
			.switchIfEmpty(Mono.error(DeleteEntityException.authorByBooksDependency(authorId)))
			.flatMap(authorRepository::deleteById);
	}

	private Mono<AuthorDto> save(AuthorFormDto authorFormDto) {
		return Mono.just(authorFormDto)
			.filterWhen(author ->
				authorRepository.existsByFullNameAndIdNot(author.getFullName(), author.getId())
					.map(exist -> !exist))
			.switchIfEmpty(Mono.error(FullNameDuplicateException.byFullName(authorFormDto.getFullName())))
			.flatMap(author -> authorRepository.save(
				Objects.requireNonNull(conversionService.convert(author, Author.class))))
			.map(AuthorDto::new);
	}
}
