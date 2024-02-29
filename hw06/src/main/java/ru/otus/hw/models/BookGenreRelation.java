package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookGenreRelation {

	private long bookId;

	private long genreId;

}
