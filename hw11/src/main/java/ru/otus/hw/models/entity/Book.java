package ru.otus.hw.models.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document("books")
@Data
@Builder
public class Book {

	@Id
	private String id;

	private String title;

	@DocumentReference
	private Author author;

	@DocumentReference
	private List<Genre> genres;

	public Book(String title, Author author, List<Genre> genres) {
		this.title = title;
		this.author = author;
		this.genres = genres;
	}

	@PersistenceCreator
	public Book(String id, String title, Author author, List<Genre> genres) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.genres = genres;
	}
}
