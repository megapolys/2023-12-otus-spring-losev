package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

	private final CommentService commentService;

	private final CommentConverter commentConverter;

	@ShellMethod(value = "Find comment by bookId", key = "cbbid")
	public String findCommentByBookId(long id) {
		return commentService.findByBookId(id).stream()
			.map(commentConverter::commentToString)
			.collect(Collectors.joining("," + System.lineSeparator()));
	}

	@ShellMethod(value = "Find comment by id", key = "cbid")
	public String findCommentById(long id) {
		return commentService.findById(id).stream()
			.map(commentConverter::commentToString)
			.collect(Collectors.joining("," + System.lineSeparator()));
	}

	@ShellMethod(value = "Insert comment", key = "ci")
	public String insertComment(String text, long bookId) {
		Comment comment = commentService.create(text, bookId);
		return commentConverter.commentToString(comment);
	}

	@ShellMethod(value = "Update comment", key = "cu")
	public String updateComment(long id, String text) {
		Comment comment = commentService.update(id, text);
		return commentConverter.commentToString(comment);
	}

	@ShellMethod(value = "Delete comment", key = "cd")
	public void deleteComment(long id) {
		commentService.deleteById(id);
	}
}
