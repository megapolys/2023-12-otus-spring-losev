package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.jpa.Comment;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.services.IdTransformerService;

@Service
@StepScope
@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, MongoComment> {

	private final IdTransformerService idTransformerService;

	private final BookProcessor bookProcessor;

	@Override
	public MongoComment process(Comment comment) throws Exception {
		String newId = idTransformerService.saveId("comment", comment.getId());
		MongoBook book = bookProcessor.process(comment.getBook());
		return new MongoComment(newId, comment.getText(), book);
	}
}
