package ru.otus.hw.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import reactor.core.publisher.Flux;

@Document("books")
@AllArgsConstructor
@Data
@Builder
public class Book {

	@Id
	private String id;

	private String title;

	@DocumentReference
	private Author author;

	@DocumentReference
	private Flux<Genre> genres;

	public Book(String title, Author author, Flux<Genre> genres) {
		this.title = title;
		this.author = author;
		this.genres = genres;
	}
}
