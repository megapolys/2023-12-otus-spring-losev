package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.CommentDto;

@Component
public class CommentConverter {
	public String commentToString(CommentDto comment) {
		return "Id: %s, Text: %s, BookId: %s"
			.formatted(comment.getId(), comment.getText(), comment.getBook().getId());
	}
}
