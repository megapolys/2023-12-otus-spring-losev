package ru.otus.hw.generator;

import ru.otus.hw.models.dto.AuthorDto;

import java.util.List;

public class AuthorDtoGenerator {

	public static List<AuthorDto> generateList() {
		return List.of(
			new AuthorDto(1, "name_1"),
			new AuthorDto(2, "name_2")
		);
	}

	public static AuthorDto generate() {
		return new AuthorDto(1, "name_1");
	}
}
