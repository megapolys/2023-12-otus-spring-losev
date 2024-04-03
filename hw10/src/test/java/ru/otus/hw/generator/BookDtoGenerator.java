package ru.otus.hw.generator;

import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;

public class BookDtoGenerator {

	public static List<BookDto> generateList() {
		return List.of(
			new BookDto(1, "title_1", AuthorDtoGenerator.generate(),
				List.of(new GenreDto(1, "genre_1"))
			),
			new BookDto(2, "title_2", AuthorDtoGenerator.generate(),
				List.of(new GenreDto(2, "genre_2"), new GenreDto(3, "genre_3"))
			)
		);
	}

	public static BookDto generate() {
		return new BookDto(1, "title_1", AuthorDtoGenerator.generate(),
			List.of(new GenreDto(1, "genre_1"), new GenreDto(2, "genre_2"))
		);
	}

}
