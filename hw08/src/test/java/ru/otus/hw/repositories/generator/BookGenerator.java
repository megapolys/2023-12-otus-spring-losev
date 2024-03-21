package ru.otus.hw.repositories.generator;

import ru.otus.hw.models.entity.Book;

import java.util.List;
import java.util.Set;

public class BookGenerator {

	public static Book generateFirst() {
		return new Book("1", "BookTitle_1", AuthorGenerator.generateFirst(), Set.of("Genre_1", "Genre_2"));
	}

	public static Book generateSecond() {
		return new Book("2", "BookTitle_2", AuthorGenerator.generateFirst(), Set.of("Genre_3", "Genre_4"));
	}

	public static Book generateThird() {
		return new Book("3", "BookTitle_3", AuthorGenerator.generateSecond(), Set.of("Genre_5", "Genre_6"));
	}

	public static Book generateNew() {
		return Book.builder()
			.title("BookTitle_4")
			.author(AuthorGenerator.generateSecond())
			.genres(Set.of("Genre_1"))
			.build();
	}

	public static Book generateUpdate() {
		return Book.builder()
			.id("1")
			.title("BookTitle_4")
			.author(AuthorGenerator.generateSecond())
			.genres(Set.of("Genre_1"))
			.build();
	}

	public static List<Book> generateList() {
		return List.of(generateFirst(), generateSecond(), generateThird());
	}
}
