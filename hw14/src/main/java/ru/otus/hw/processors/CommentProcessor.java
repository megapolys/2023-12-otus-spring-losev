package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.jpa.Comment;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;

@Service
@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, MongoComment> {

	private final BookProcessor bookProcessor;

	@Override
	public MongoComment process(Comment comment) throws Exception {
		String newId = String.valueOf(comment.getId());
		MongoBook book = bookProcessor.process(comment.getBook());
		return new MongoComment(newId, comment.getText(), book);
	}
}
