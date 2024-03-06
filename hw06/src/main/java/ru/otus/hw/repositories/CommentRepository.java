package ru.otus.hw.repositories;

import ru.otus.hw.models.entity.Comment;

import java.util.List;

public interface CommentRepository {

	Comment findById(long id);

	List<Comment> findByBookId(long bookId);

	Comment save(Comment comment);

	void deleteById(long id);

}
