package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.BookDto;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
	private final AuthorConverter authorConverter;

	public String bookToString(BookDto book) {
		var genresString = book.getGenres().stream()
			.map("{%s}"::formatted)
			.collect(Collectors.joining(", "));
		return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
			book.getId(),
			book.getTitle(),
			authorConverter.authorToString(book.getAuthor()),
			genresString
		);
	}
}
