package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.entity.Book;

import java.util.List;

@Data
@AllArgsConstructor
public class BookDto {

	private long id;

	private String title;

	private AuthorDto author;

	private List<GenreDto> genres;

	public BookDto(Book book) {
		this(
			book.getId(),
			book.getTitle(),
			new AuthorDto(book.getAuthor()),
			book.getGenres().stream()
				.map(GenreDto::new)
				.toList()
		);
	}

}
