package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;

	private final BookRepository bookRepository;

	@Override
	@Transactional
	public Optional<CommentDto> findById(long id) {
		return Optional.ofNullable(commentRepository.findById(id))
			.map(CommentDto::new);
	}

	@Override
	@Transactional
	public List<CommentDto> findByBookId(long bookId) {
		return commentRepository.findByBookId(bookId).stream()
			.map(CommentDto::new)
			.toList();
	}

	@Override
	@Transactional
	public CommentDto create(String text, long bookId) {
		Optional<Book> book = bookRepository.findById(bookId);
		if (book.isEmpty()) {
			throw new EntityNotFoundException("Book with id %d not found".formatted(bookId));
		}
		Comment comment = new Comment(0, text, book.get());
		Comment saved = commentRepository.save(comment);
		return new CommentDto(saved);
	}

	@Override
	@Transactional
	public CommentDto update(long id, String text) {
		Comment comment;
		try {
			comment = commentRepository.findById(id);
		} catch (NoResultException e) {
			throw new EntityNotFoundException("Comment with id %d not found".formatted(id));
		}
		comment.setText(text);
		Comment saved = commentRepository.save(comment);
		return new CommentDto(saved);
	}

	@Override
	@Transactional
	public void deleteById(long id) {
		commentRepository.deleteById(id);
	}
}
