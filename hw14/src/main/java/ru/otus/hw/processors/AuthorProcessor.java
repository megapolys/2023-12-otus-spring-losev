package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.jpa.Author;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.services.IdTransformerService;

@Service
@StepScope
@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<Author, MongoAuthor> {

	private final IdTransformerService idTransformerService;

	@Override
	public MongoAuthor process(Author author) throws Exception {
		String newId = idTransformerService.saveId("author", author.getId());
		return new MongoAuthor(newId, author.getFullName());
	}
}
