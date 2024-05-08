package ru.otus.hw.models.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("genres")
public class Genre {
	@Id
	private String id;

	private String name;

	@PersistenceCreator
	public Genre(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Genre(String name) {
		this.name = name;
	}
}
