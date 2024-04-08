package ru.otus.hw.generator;

import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.AuthorFormDto;
import ru.otus.hw.models.entity.Author;

import java.util.List;

public class AuthorFormDtoGenerator {

	public static List<AuthorFormDto> generateList() {
		return List.of(
			new AuthorFormDto(1, "name_1"),
			new AuthorFormDto(2, "name_2")
		);
	}

	public static AuthorFormDto generate() {
		return new AuthorFormDto(1, "name_1");
	}

	public static AuthorFormDto generateWithEmptyFullName() {
		return new AuthorFormDto(1, "");
	}

	public static AuthorFormDto generateNewAuthor() {
		return new AuthorFormDto(0, "name_1");
	}
}
