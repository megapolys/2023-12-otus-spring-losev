package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entity.Book;

@Service
@RequiredArgsConstructor
public class BookDtoToBookConverter implements Converter<BookDto, Book> {

	private final AuthorDtoToAuthorConverter authorConverter;

	private final GenreDtoToGenreConverter genreConverter;

	@Override
	public Book convert(BookDto bookDto) {
		return new Book(bookDto.getId(), bookDto.getTitle(), authorConverter.convert(bookDto.getAuthor()),
			bookDto.getGenres().stream().map(genreConverter::convert).toList());
	}
}
