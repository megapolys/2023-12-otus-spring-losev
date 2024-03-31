package ru.otus.hw.repositories.generator;

import ru.otus.hw.models.entity.Genre;

import java.util.List;

public class GenreGenerator {

	public static List<Genre> generateList() {
		return List.of(new Genre("Genre_1"),
			new Genre("Genre_2"),
			new Genre("Genre_3"),
			new Genre("Genre_4"),
			new Genre("Genre_5"),
			new Genre("Genre_6"));
	}
}
