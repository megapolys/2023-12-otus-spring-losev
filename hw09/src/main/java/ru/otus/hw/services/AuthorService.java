package ru.otus.hw.services;

import ru.otus.hw.models.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
	List<AuthorDto> findAll();

	void save(AuthorDto authorDto);

	AuthorDto findById(long id);

	void deleteById(long id);
}
