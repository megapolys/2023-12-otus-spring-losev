package ru.otus.hw.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-author-genres-graph",
	attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genres")})
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "title")
	private String title;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Author.class)
	@JoinColumn(name = "author_id")
	private Author author;

	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Genre.class)
	@JoinTable(name = "books_genres", joinColumns = @JoinColumn(name = "book_id"),
		inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private List<Genre> genres;
}
