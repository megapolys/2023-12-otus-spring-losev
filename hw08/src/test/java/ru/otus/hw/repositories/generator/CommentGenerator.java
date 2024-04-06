package ru.otus.hw.repositories.generator;

import ru.otus.hw.models.entity.Comment;

import java.util.List;

public class CommentGenerator {

	public static Comment generate() {
		return new Comment("1", "Comment_1", BookGenerator.generateFirst());
	}

	public static List<Comment> generateByFirstBook() {
		return List.of(
			new Comment("1", "Comment_1", BookGenerator.generateFirst()),
			new Comment("2", "Comment_2", BookGenerator.generateFirst())
		);
	}
}
