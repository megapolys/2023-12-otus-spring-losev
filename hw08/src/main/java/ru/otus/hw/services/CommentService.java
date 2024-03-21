package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

	Optional<CommentDto> findById(String id);

	List<CommentDto> findByBookId(String bookId);

	CommentDto create(String text, String bookId);

	CommentDto update(String id, String text);

	void deleteById(String id);
}
