package ru.otus.hw.config.generator;


import ru.otus.hw.model.mongo.MongoGenre;

import java.util.List;

public class MongoGenreGenerator {

	public static List<MongoGenre> generateList() {
		return List.of(
			new MongoGenre("Genre_1"),
			new MongoGenre("Genre_2"),
			new MongoGenre("Genre_3"),
			new MongoGenre("Genre_4"),
			new MongoGenre("Genre_5"),
			new MongoGenre("Genre_6")
		);
	}
}
