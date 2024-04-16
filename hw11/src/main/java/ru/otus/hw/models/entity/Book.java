package ru.otus.hw.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Set;

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
	private Set<Genre> genres;
}
