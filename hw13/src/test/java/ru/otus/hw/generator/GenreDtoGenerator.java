package ru.otus.hw.generator;

import ru.otus.hw.models.dto.GenreDto;

import java.util.List;

public class GenreDtoGenerator {

	public static List<GenreDto> generateList() {
		return List.of(new GenreDto(1, "genre_1"),
			new GenreDto(2, "genre_2"));
	}
}
