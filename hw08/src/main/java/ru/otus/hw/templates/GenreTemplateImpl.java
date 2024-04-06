package ru.otus.hw.templates;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreTemplateImpl implements GenreTemplate {
	private final MongoTemplate mongoTemplate;

	@Override
	public List<Genre> findAll() {
		Query query = new Query();
		query.addCriteria(Criteria.where("genres").exists(true));
		return mongoTemplate.findDistinct(query, "genres", Book.class, Genre.class);
	}
}

