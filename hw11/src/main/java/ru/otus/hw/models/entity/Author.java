package ru.otus.hw.models.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("authors")
@Data
@Builder
public class Author {
	@Id
	private String id;

	private String fullName;

	public Author(String fullName) {
		this.fullName = fullName;
	}

	@PersistenceCreator
	public Author(String id, String fullName) {
		this.id = id;
		this.fullName = fullName;
	}
}
