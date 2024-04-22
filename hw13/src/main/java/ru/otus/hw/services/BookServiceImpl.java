package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
	private final AuthorRepository authorRepository;

	private final GenreRepository genreRepository;

	private final BookRepository bookRepository;

	@Override
	@Transactional(readOnly = true)
	public BookDto findById(long id) {
		Book book = bookRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Book with id %d not found.".formatted(id)));
		return new BookDto(book);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> findAll() {
		return bookRepository.findAll().stream()
			.map(BookDto::new)
			.toList();
	}

	@Override
	@Transactional
	public BookDto save(BookFormDto bookFormDto) {
		Optional<Author> author = authorRepository.findById(bookFormDto.getAuthorId());
		List<Genre> genres = genreRepository.findAllByIds(bookFormDto.getGenreIds());
		if (isEmpty(genres) || bookFormDto.getGenreIds().size() != genres.size()) {
			throw new EntityNotFoundException("One or all genres with ids %s not found"
				.formatted(bookFormDto.getGenreIds()));
		}
		if (author.isEmpty()) {
			throw new EntityNotFoundException("Author with id %s not found"
				.formatted(bookFormDto.getAuthorId()));
		}

		Book book = new Book(bookFormDto.getId(), bookFormDto.getTitle(), author.get(), genres);
		Book saved = bookRepository.save(book);
		return new BookDto(saved);
	}

	@Override
	@Transactional
	public void deleteById(long id) {
		bookRepository.deleteById(id);
	}
}
