package ru.otus.hw.config.generator;


import ru.otus.hw.model.mongo.MongoAuthor;

import java.util.List;

public class MongoAuthorGenerator {

	public static List<MongoAuthor> generateList() {
		return List.of(
			new MongoAuthor("Author_1"),
			new MongoAuthor("Author_2"),
			new MongoAuthor("Author_3")
		);
	}
}
