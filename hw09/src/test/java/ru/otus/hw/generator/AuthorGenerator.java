package ru.otus.hw.generator;

import ru.otus.hw.models.entity.Author;

import java.util.List;

public class AuthorGenerator {

	public static List<Author> generateList() {
		return List.of(
			new Author(2, "name_2"),
			new Author(1, "name_1")
		);
	}

	public static Author generate() {
		return new Author(1, "name_1");
	}
}
