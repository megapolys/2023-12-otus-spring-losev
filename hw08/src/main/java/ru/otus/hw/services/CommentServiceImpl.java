package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
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
	@Transactional(readOnly = true)
	public Optional<CommentDto> findById(String id) {
		return commentRepository.findById(id)
			.map(CommentDto::new);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentDto> findByBookId(String bookId) {
		return commentRepository.findByBookId(bookId).stream()
			.map(CommentDto::new)
			.toList();
	}

	@Override
	@Transactional
	public CommentDto create(String text, String bookId) {
		Optional<Book> book = bookRepository.findById(bookId);
		if (book.isEmpty()) {
			throw new EntityNotFoundException("Book with id %d not found".formatted(bookId));
		}
		Comment comment = new Comment(text, book.get());
		Comment saved = commentRepository.save(comment);
		return new CommentDto(saved);
	}

	@Override
	@Transactional
	public CommentDto update(String id, String text) {
		Comment comment = commentRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
		comment.setText(text);
		Comment saved = commentRepository.save(comment);
		return new CommentDto(saved);
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		commentRepository.deleteById(id);
	}
}
