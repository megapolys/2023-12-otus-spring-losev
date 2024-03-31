package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entity.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, String> {

	List<Comment> findByBookId(String bookId);

	void deleteAllByBookId(String bookId);
}
