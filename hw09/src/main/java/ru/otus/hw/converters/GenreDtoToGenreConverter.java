package ru.otus.hw.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.entity.Genre;

@Service
public class GenreDtoToGenreConverter implements Converter<GenreDto, Genre> {
	@Override
	public Genre convert(GenreDto genreDto) {
		return new Genre(genreDto.getId(), genreDto.getName());
	}
}
