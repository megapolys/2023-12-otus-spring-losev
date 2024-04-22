package ru.otus.hw.generator;

import ru.otus.hw.models.entity.Genre;

import java.util.List;

public class GenreGenerator {

	public static List<Genre> generateList() {
		return List.of(new Genre(1, "genre_1"),
			new Genre(2, "genre_2"));
	}
}
