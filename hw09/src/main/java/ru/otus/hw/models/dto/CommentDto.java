package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.entity.Comment;

@Data
@AllArgsConstructor
public class CommentDto {

	private long id;

	private String text;

	private BookDto book;

	public CommentDto(Comment comment) {
		this(comment.getId(), comment.getText(), new BookDto(comment.getBook()));
	}
}
