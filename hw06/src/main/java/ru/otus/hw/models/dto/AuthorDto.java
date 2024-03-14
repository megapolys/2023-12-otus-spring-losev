package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.entity.Author;

@Data
@AllArgsConstructor
public class AuthorDto {

	private long id;

	private String fullName;

	public AuthorDto(Author author) {
		this(author.getId(), author.getFullName());
	}
}
