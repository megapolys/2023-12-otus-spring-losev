package ru.otus.hw.repositories.generator;

import ru.otus.hw.models.entity.Author;

public class AuthorGenerator {

	public static Author generateFirst() {
		return new Author("1", "Author_1");
	}

	public static Author generateSecond() {
		return new Author("2", "Author_2");
	}

}
