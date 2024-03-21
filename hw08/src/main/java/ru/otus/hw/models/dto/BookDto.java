package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.entity.Book;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class BookDto {

	private String id;

	private String title;

	private AuthorDto author;

	private Set<String> genres;

	public BookDto(Book book) {
		this(
			book.getId(),
			book.getTitle(),
			new AuthorDto(book.getAuthor()),
			book.getGenres()
		);
	}

}
