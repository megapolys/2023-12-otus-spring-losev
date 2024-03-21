package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.AuthorDto;

@Component
public class AuthorConverter {
	public String authorToString(AuthorDto author) {
		return "Id: %s, FullName: %s".formatted(author.getId(), author.getFullName());
	}
}
