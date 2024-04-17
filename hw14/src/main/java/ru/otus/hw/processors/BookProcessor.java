package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.jpa.Book;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoGenre;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<Book, MongoBook> {

	private final AuthorProcessor authorProcessor;

	private final GenreProcessor genreProcessor;

	@Override
	public MongoBook process(Book book) throws Exception {
		String newId = String.valueOf(book.getId());
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
