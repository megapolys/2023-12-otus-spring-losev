package ru.otus.hw.services;

import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.AuthorFormDto;

import java.util.List;

public interface AuthorService {
	List<AuthorDto> findAll();

	void save(AuthorFormDto author);

	AuthorDto findById(long id);

	void deleteById(long id);
}
