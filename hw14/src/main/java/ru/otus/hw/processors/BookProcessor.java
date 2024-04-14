package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.jpa.Book;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.services.IdTransformerService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@StepScope
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<Book, MongoBook> {

	private final IdTransformerService idTransformerService;

	private final AuthorProcessor authorProcessor;

	private final GenreProcessor genreProcessor;

	@Override
	public MongoBook process(Book book) throws Exception {
		String newId = idTransformerService.saveId("book", book.getId());
		MongoAuthor author = authorProcessor.process(book.getAuthor());

		Set<MongoGenre> genres = book.getGenres().stream()
			.map(genre -> {
				try {
					return genreProcessor.process(genre);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			})
			.collect(Collectors.toSet());

		return new MongoBook(newId, book.getTitle(), author, genres);
	}
}
