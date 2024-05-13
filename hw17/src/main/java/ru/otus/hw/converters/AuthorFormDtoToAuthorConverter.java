package ru.otus.hw.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.AuthorFormDto;
import ru.otus.hw.models.entity.Author;

@Component
public class AuthorFormDtoToAuthorConverter implements Converter<AuthorFormDto, Author> {

	@Override
	public Author convert(AuthorFormDto authorFormDto) {
		return Author.builder()
			.id(authorFormDto.getId())
			.fullName(authorFormDto.getFullName())
			.build();
	}
}
