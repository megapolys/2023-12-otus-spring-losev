package ru.otus.hw.config.generator;

import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoGenre;

import java.util.List;
import java.util.Set;

public class MongoBookGenerator {

	public static List<MongoBook> generateList() {
		return List.of(
			new MongoBook("BookTitle_1", new MongoAuthor("Author_1"), Set.of(new MongoGenre("Genre_1"), new MongoGenre("Genre_2"))),
			new MongoBook("BookTitle_2", new MongoAuthor("Author_2"), Set.of(new MongoGenre("Genre_3"), new MongoGenre("Genre_4"))),
			new MongoBook("BookTitle_3", new MongoAuthor("Author_3"), Set.of(new MongoGenre("Genre_5"), new MongoGenre("Genre_6")))
		);
	}
}
