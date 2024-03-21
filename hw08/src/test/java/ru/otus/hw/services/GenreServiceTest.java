package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Сервис для работы с жанрами")
@SpringBootTest
@AutoConfigureDataMongo
class GenreServiceTest {

	@Autowired
	GenreService genreService;

	@DisplayName("Получить все жанры")
	@Test
	void findAll() {
		List<String> expectedGenres = List.of("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6");
		List<String> actualGenres = genreService.findAll();

		assertThat(actualGenres).isEqualTo(expectedGenres);
	}
}