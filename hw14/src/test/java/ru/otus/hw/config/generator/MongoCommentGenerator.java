package ru.otus.hw.config.generator;


import ru.otus.hw.model.mongo.MongoComment;

import java.util.List;

public class MongoCommentGenerator {

	public static List<MongoComment> generateList() {
		return List.of(
			new MongoComment("Comment_1", MongoBookGenerator.generateList().get(0)),
			new MongoComment("Comment_2", MongoBookGenerator.generateList().get(0)),
			new MongoComment("Comment_3", MongoBookGenerator.generateList().get(1))
		);
	}
}
