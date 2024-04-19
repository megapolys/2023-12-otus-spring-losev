package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.entity.Book;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BookDto {

	private String id;

	private String title;

	private AuthorDto author;

	private Flux<GenreDto> genres;

	public BookDto(Book book) {
		this(
			book.getId(),
			book.getTitle(),
			new AuthorDto(book.getAuthor()),
			book.getGenres().map(GenreDto::new)
		);
	}

}
