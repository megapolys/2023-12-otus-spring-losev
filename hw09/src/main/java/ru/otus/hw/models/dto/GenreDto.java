package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.entity.Genre;

@Data
@AllArgsConstructor
public class GenreDto {

	private long id;

	private String name;

	public GenreDto(Genre genre) {
		this(genre.getId(), genre.getName());
	}

}
