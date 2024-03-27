package ru.otus.hw.services;

import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookFormDto;

import java.util.List;

public interface BookService {
	BookDto findById(long id);

	List<BookDto> findAll();

	BookDto save(BookFormDto book);

	void deleteById(long id);
}
