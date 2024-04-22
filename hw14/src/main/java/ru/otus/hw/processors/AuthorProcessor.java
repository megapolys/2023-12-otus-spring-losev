package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.jpa.Author;
import ru.otus.hw.model.mongo.MongoAuthor;

@Service
public class AuthorProcessor implements ItemProcessor<Author, MongoAuthor> {

	@Override
	public MongoAuthor process(Author author) throws Exception {
		String newId = String.valueOf(author.getId());
		return new MongoAuthor(newId, author.getFullName());
	}
}
