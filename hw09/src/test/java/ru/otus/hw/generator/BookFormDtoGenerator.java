package ru.otus.hw.generator;

import ru.otus.hw.models.dto.BookFormDto;

import java.util.Set;

public class BookFormDtoGenerator {

	public static BookFormDto generate() {
		return new BookFormDto(1, "title_1", 1, Set.of(1L, 2L));
	}

	public static BookFormDto generateWithEmptyTitle() {
		return new BookFormDto(1, "", 1, Set.of(1L, 2L));
	}
}
