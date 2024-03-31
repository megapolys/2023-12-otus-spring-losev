package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.templates.GenreTemplate;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
	private final GenreTemplate genreTemplate;

	@Override
	public List<GenreDto> findAll() {
		return genreTemplate.findAll().stream()
			.map(GenreDto::new)
			.sorted(Comparator.comparing(GenreDto::getName))
			.toList();
	}
}
