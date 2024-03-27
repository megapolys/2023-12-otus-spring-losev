package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.DeleteEntityException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.author.FullNameDuplicateException;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;

	private final BookRepository bookRepository;

	private final ConversionService conversionService;

	@Override
	public List<AuthorDto> findAll() {
		return authorRepository.findAll().stream()
			.map(AuthorDto::new)
			.sorted(Comparator.comparing(AuthorDto::getFullName))
			.toList();
	}

	@Override
	@Transactional
	public void save(AuthorDto authorDto) {
		if (authorRepository.existsByFullNameAndIdNot(authorDto.getFullName(), authorDto.getId())) {
			throw new FullNameDuplicateException("Author with same fullName %s already exists"
				.formatted(authorDto.getFullName()));
		}
		authorRepository.save(Objects.requireNonNull(conversionService.convert(authorDto, Author.class)));

	}

	@Override
	public AuthorDto findById(long id) {
		Author author = authorRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Author with id %d not found.".formatted(id)));
		return new AuthorDto(author);
	}

	@Override
	@Transactional
	public void deleteById(long id) {
		Author author = authorRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Author with id %d not found.".formatted(id)));
		if (bookRepository.existsBookByAuthor(author)) {
			throw DeleteEntityException.authorByBooksDependency(id);
		}
		authorRepository.deleteById(id);
	}
}
