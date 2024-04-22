package ru.otus.hw.model.mongo;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("authors")
@Data
public class MongoAuthor {
	@Id
	@EqualsAndHashCode.Exclude
	private String id;

	private String fullName;

	public MongoAuthor(String fullName) {
		this.fullName = fullName;
	}

	@PersistenceCreator
	public MongoAuthor(String id, String fullName) {
		this.id = id;
		this.fullName = fullName;
	}
}
