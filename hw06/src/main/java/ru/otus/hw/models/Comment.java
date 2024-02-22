package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "comments")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "text")
	private String text;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private Book book;
}
