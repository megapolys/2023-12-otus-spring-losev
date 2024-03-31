package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
	private final AuthorRepository authorRepository;

	private final BookRepository bookRepository;

	private final CommentRepository commentRepository;

	@Override
	@Transactional(readOnly = true)
	public Optional<BookDto> findById(String id) {
		return bookRepository.findById(id)
			.map(BookDto::new);
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
	public BookDto insert(String title, String authorId, Set<String> genres) {
		return save(null, title, authorId, genres);
	}

	@Override
	@Transactional
	public BookDto update(String id, String title, String authorId, Set<String> genres) {
		return save(id, title, authorId, genres);
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		commentRepository.deleteAllByBookId(id);
		bookRepository.deleteById(id);
	}

	private BookDto save(String id, String title, String authorId, Set<String> genreNames) {
		if (isEmpty(genreNames)) {
			throw new IllegalArgumentException("Genres must not be empty");
		}

		var author = authorRepository.findById(authorId);
		if (author.isEmpty()) {
			throw new EntityNotFoundException("Author with id %s not found".formatted(authorId));
		}

		Set<Genre> genres = genreNames.stream().map(Genre::new).collect(Collectors.toSet());
		Book book = new Book(id, title, author.get(), genres);
		Book saved = bookRepository.save(book);
		return new BookDto(saved);
	}
}
