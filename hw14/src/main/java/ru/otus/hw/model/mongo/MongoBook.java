package ru.otus.hw.model.mongo;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Set;

@Document("books")
@Data
@Builder
public class MongoBook {

	@Id
	private String id;

	private String title;

	@DocumentReference
	private MongoAuthor author;

	@DocumentReference
	private Set<MongoGenre> genres;

	@PersistenceCreator
	public MongoBook(String id, String title, MongoAuthor author, Set<MongoGenre> genres) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.genres = genres;
	}

	public MongoBook(String title, MongoAuthor author, Set<MongoGenre> genres) {
		this.title = title;
		this.author = author;
		this.genres = genres;
	}
}
