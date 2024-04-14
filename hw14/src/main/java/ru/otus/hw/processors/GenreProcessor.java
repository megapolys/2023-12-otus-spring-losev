package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.jpa.Genre;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.services.IdTransformerService;

@Service
@StepScope
@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<Genre, MongoGenre> {

	private final IdTransformerService idTransformerService;

	@Override
	public MongoGenre process(Genre genre) throws Exception {
		String newId = idTransformerService.saveId("genre", genre.getId());
		return new MongoGenre(newId, genre.getName());
	}
}
