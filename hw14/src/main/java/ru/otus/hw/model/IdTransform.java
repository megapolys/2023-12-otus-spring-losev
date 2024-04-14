package ru.otus.hw.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("id_transform")
@Data
public class IdTransform {
	@Id
	private String id;

	private String tableName;

	private long oldId;

	public IdTransform(String tableName, long oldId) {
		this.tableName = tableName;
		this.oldId = oldId;
	}

	@PersistenceCreator
	public IdTransform(String id, String tableName, long oldId) {
		this.id = id;
		this.tableName = tableName;
		this.oldId = oldId;
	}
}
