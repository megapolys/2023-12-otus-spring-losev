package ru.otus.hw.models.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document("comments")
@Data
public class Comment {
	@Id
	private String id;

	private String text;

	@DocumentReference
	private Book book;

	public Comment(String text, Book book) {
		this.text = text;
		this.book = book;
	}

	@PersistenceCreator
	public Comment(String id, String text, Book book) {
		this.id = id;
		this.text = text;
		this.book = book;
	}
}
