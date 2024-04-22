package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.entity.Book;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

	private long id;

	@NotBlank
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
