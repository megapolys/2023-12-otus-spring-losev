package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
	private final AuthorRepository authorRepository;

	private final GenreRepository genreRepository;

	private final BookRepository bookRepository;

	@Override
	@Transactional
	public Optional<BookDto> findById(long id) {
		return bookRepository.findById(id)
			.map(BookDto::new);
	}

	@Override
	@Transactional
	public List<BookDto> findAll() {
		return bookRepository.findAll().stream()
			.map(BookDto::new)
			.toList();
	}

	@Override
	@Transactional
	public BookDto insert(String title, long authorId, Set<Long> genresIds) {
		return save(0, title, authorId, genresIds);
	}

	@Override
	@Transactional
	public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
		return save(id, title, authorId, genresIds);
	}

	@Override
	@Transactional
	public void deleteById(long id) {
		bookRepository.deleteById(id);
	}

	private BookDto save(long id, String title, long authorId, Set<Long> genresIds) {
		if (isEmpty(genresIds)) {
			throw new IllegalArgumentException("Genres ids must not be null");
		}

		var author = authorRepository.findById(authorId);
		var genres = genreRepository.findAllByIds(genresIds);
		if (isEmpty(genres) || genresIds.size() != genres.size()) {
			throw new EntityNotFoundException("One or all genres with ids %s not found"
				.formatted(genresIds));
		}

		Book book = new Book(id, title, author, genres);
		Book saved = bookRepository.save(book);
		return new BookDto(saved);
	}
}
