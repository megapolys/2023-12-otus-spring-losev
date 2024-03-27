package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.entity.Author;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

	private long id;

	@NotBlank
	private String fullName;

	public AuthorDto(Author author) {
		this(author.getId(), author.getFullName());
	}
}
