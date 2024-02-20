package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
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
	public Optional<Comment> findById(long id) {
		return Optional.ofNullable(commentRepository.findById(id));
	}

	@Override
	public List<Comment> findByBookId(long bookId) {
		return commentRepository.findByBookId(bookId);
	}

	@Override
	@Transactional
	public Comment create(String text, long bookId) {
		Book book;
		try {
			book = bookRepository.findById(bookId);
		} catch (NoResultException e) {
			throw new EntityNotFoundException("Book with id %d not found".formatted(bookId));
		}
		Comment comment = new Comment(0, text, book);
		return commentRepository.save(comment);
	}

	@Override
	@Transactional
	public Comment update(long id, String text) {
		Comment comment;
		try {
			comment = commentRepository.findById(id);
		} catch (NoResultException e) {
			throw new EntityNotFoundException("Comment with id %d not found".formatted(id));
		}
		comment.setText(text);
		return commentRepository.save(comment);
	}

	@Override
	@Transactional
	public void deleteById(long id) {
		commentRepository.deleteById(id);
	}
}
