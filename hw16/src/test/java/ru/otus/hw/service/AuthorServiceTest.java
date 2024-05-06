package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.exceptions.DeleteEntityException;
import ru.otus.hw.exceptions.author.FullNameDuplicateException;
import ru.otus.hw.generator.AuthorDtoGenerator;
import ru.otus.hw.generator.AuthorFormDtoGenerator;
import ru.otus.hw.generator.AuthorGenerator;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.AuthorFormDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthorServiceTest extends AbstractServiceTest {

	@Autowired
	private AuthorService authorService;

	@Test
	void whenFindAllThenReturn() {
		List<Author> authors = AuthorGenerator.generateList();
		List<AuthorDto> expectedAuthors = AuthorDtoGenerator.generateList();

		when(authorRepository.findAll()).thenReturn(authors);

		List<AuthorDto> actual = authorService.findAll();
		
		then(actual).isEqualTo(expectedAuthors);

		verify(authorRepository).findAll();
	}

	@Test
	void whenFindAllThenReturnEmptyList() {
		when(authorRepository.findAll()).thenReturn(List.of());

		List<AuthorDto> actual = authorService.findAll();

		then(actual).isEqualTo(List.of());

		verify(authorRepository).findAll();
	}

	@Test
	void whenFindByIdThenReturn() {
		Author author = AuthorGenerator.generate();
		AuthorDto expectedAuthor = AuthorDtoGenerator.generate();

		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

		AuthorDto actual = authorService.findById(author.getId());
		
		then(actual).isEqualTo(expectedAuthor);

		verify(authorRepository).findById(author.getId());
	}

	@Test
	void whenSaveAuthorThenSave() {
		Author author = AuthorGenerator.generateNewAuthor();
		AuthorFormDto authorFormDto = AuthorFormDtoGenerator.generateNewAuthor();

		when(authorRepository.existsByFullNameAndIdNot(author.getFullName(), author.getId())).thenReturn(false);

		authorService.save(authorFormDto);

		verify(authorRepository).save(author);
	}

	@Test
	void whenSaveExistsAuthorThenUpdate() {
		Author author = AuthorGenerator.generate();
		AuthorFormDto authorFormDto = AuthorFormDtoGenerator.generate();

		when(authorRepository.existsByFullNameAndIdNot(author.getFullName(), author.getId())).thenReturn(false);

		authorService.save(authorFormDto);

		verify(authorRepository).save(author);
	}

	@Test
	void whenTrySaveAuthorWithDuplicateFullNameThenException() {
		Author author = AuthorGenerator.generateNewAuthor();
		AuthorFormDto authorFormDto = AuthorFormDtoGenerator.generateNewAuthor();

		when(authorRepository.existsByFullNameAndIdNot(author.getFullName(), author.getId())).thenReturn(true);

		assertThatThrownBy(() -> {
			authorService.save(authorFormDto);
		}).isInstanceOf(FullNameDuplicateException.class)
			.hasMessage("Author with same fullName name_1 already exists");

		verify(authorRepository).existsByFullNameAndIdNot(author.getFullName(), author.getId());
		verify(authorRepository, never()).save(any());
	}

	@Test
	void whenDeleteAuthorThenDelete() {
		Author author = AuthorGenerator.generate();

		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
		when(bookRepository.existsBookByAuthor(author)).thenReturn(false);

		authorService.deleteById(author.getId());

		verify(authorRepository).deleteById(author.getId());
	}

	@Test
	void whenTryDeleteAuthorBookDependsThenException() {
		Author author = AuthorGenerator.generate();

		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
		when(bookRepository.existsBookByAuthor(author)).thenReturn(true);

		assertThatThrownBy(() -> {
			authorService.deleteById(author.getId());
		}).isInstanceOf(DeleteEntityException.class)
			.hasMessage("There are books by author with id 1");

		verify(authorRepository, never()).deleteById(any());
	}

}
