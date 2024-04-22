package ru.otus.hw.generator;

import ru.otus.hw.models.dto.BookFormDto;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookFormDtoGenerator {

	public static BookFormDto generate() {
		return new BookFormDto(1, "title_1", 1, Stream.of(1L, 2L)
			.collect(Collectors.toCollection(LinkedHashSet::new)));
	}

	public static BookFormDto generateInvalid() {
		return new BookFormDto(1, "", 0, Set.of());
	}
}
