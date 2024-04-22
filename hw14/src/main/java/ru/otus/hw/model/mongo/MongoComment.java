package ru.otus.hw.model.mongo;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document("comments")
@Data
public class MongoComment {
	@Id
	private String id;

	private String text;

	@DocumentReference
	private MongoBook book;

	@PersistenceCreator
	public MongoComment(String id, String text, MongoBook book) {
		this.id = id;
		this.text = text;
		this.book = book;
	}

	public MongoComment(String text, MongoBook book) {
		this.text = text;
		this.book = book;
	}
}
