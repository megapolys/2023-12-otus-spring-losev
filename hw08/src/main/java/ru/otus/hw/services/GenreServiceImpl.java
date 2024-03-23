package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entity.Book;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
	private final MongoTemplate mongoTemplate;

	@Override
	public List<String> findAll() {
		Query query = new Query();
		query.addCriteria(Criteria.where("genres").exists(true));
		List<String> genres = mongoTemplate.findDistinct(query, "genres", Book.class, String.class);
		genres.sort(String::compareTo);
		return genres;
	}
}
