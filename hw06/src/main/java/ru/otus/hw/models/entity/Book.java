package ru.otus.hw.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-author-graph", attributeNodes = {@NamedAttributeNode("author")})
@NamedEntityGraph(name = "book-author-genres-graph", attributeNodes = {@NamedAttributeNode("author"),
	@NamedAttributeNode("genres")})
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "title")
	private String title;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Author.class)
	@JoinColumn(name = "author_id")
	private Author author;

	@Fetch(FetchMode.SUBSELECT)
	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Genre.class)
	@JoinTable(name = "books_genres", joinColumns = @JoinColumn(name = "book_id"),
		inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private List<Genre> genres;
}
