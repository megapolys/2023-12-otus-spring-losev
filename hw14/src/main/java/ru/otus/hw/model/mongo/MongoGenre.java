package ru.otus.hw.model.mongo;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("genres")
@Data
public class MongoGenre {

	@Id
	@EqualsAndHashCode.Exclude
	private String id;

	private String name;

	public MongoGenre(String name) {
		this.name = name;
	}

	@PersistenceCreator
	public MongoGenre(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
