package ru.otus.hw.generator;

import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Genre;

import java.util.List;

public class BookGenerator {

	public static List<Book> generateList() {
		return List.of(
			new Book(1, "title_1", AuthorGenerator.generate(),
				List.of(new Genre(1, "genre_1"))
			),
			new Book(2, "title_2", AuthorGenerator.generate(),
				List.of(new Genre(2, "genre_2"), new Genre(3, "genre_3"))
			)
		);
	}

	public static Book generate() {
		return new Book(1, "title_1", AuthorGenerator.generate(),
				List.of(new Genre(1, "genre_1"), new Genre(2, "genre_2"))
			);
	}

}
