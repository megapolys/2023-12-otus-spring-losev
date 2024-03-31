package ru.otus.hw.templates;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.models.entity.Genre;
import ru.otus.hw.repositories.generator.GenreGenerator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с жанрами")
@DataMongoTest
@ComponentScan(basePackages = {"ru.otus.hw"})
class GenreTemplateTest {

	@Autowired
	GenreTemplate genreTemplate;

	@DisplayName("Получить все жанры")
	@Test
	void findAll() {
		List<Genre> expectedGenres = GenreGenerator.generateList();
		List<Genre> actualGenres = genreTemplate.findAll();

		assertThat(actualGenres).isEqualTo(expectedGenres);
	}
}