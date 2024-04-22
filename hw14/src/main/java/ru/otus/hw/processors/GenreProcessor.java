package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.jpa.Genre;
import ru.otus.hw.model.mongo.MongoGenre;

@Service
public class GenreProcessor implements ItemProcessor<Genre, MongoGenre> {

	@Override
	public MongoGenre process(Genre genre) throws Exception {
		String newId = String.valueOf(genre.getId());
		return new MongoGenre(newId, genre.getName());
	}
}
