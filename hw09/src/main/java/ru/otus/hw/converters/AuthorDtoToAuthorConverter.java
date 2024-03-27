package ru.otus.hw.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.entity.Author;

@Component
public class AuthorDtoToAuthorConverter implements Converter<AuthorDto, Author> {

	@Override
	public Author convert(AuthorDto authorDto) {
		return Author.builder()
			.id(authorDto.getId())
			.fullName(authorDto.getFullName())
			.build();
	}
}
