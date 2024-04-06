package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.GenreDto;

@Component
public class GenreConverter {
	public String genreToString(GenreDto genre) {
		return "%s".formatted(genre.getName());
	}
}
