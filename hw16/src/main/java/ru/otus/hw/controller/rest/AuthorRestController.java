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
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.AuthorFormDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {

	private final AuthorService authorService;

	@GetMapping("api/v1/authors")
	public List<AuthorDto> getAuthors() {
		return authorService.findAll();
	}

	@GetMapping(value = "api/v1/authors/{id}")
	public AuthorDto getAuthorById(@PathVariable long id) {
		return authorService.findById(id);
	}

	@PostMapping(value = "api/v1/authors")
	public void addAuthor(@RequestBody @Valid AuthorFormDto author) {
		authorService.save(author);
	}

	@PutMapping(value = "api/v1/authors/{id}")
	public void updateAuthor(
		@PathVariable long id,
		@RequestBody @Valid AuthorFormDto author
	) {
		author.setId(id);
		authorService.save(author);
	}

	@DeleteMapping(value = "api/v1/authors/{id}")
	public void deleteAuthor(@PathVariable("id") long id) {
		authorService.deleteById(id);
	}
}
