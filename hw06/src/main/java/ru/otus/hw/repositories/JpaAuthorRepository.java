package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.entity.Author;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaAuthorRepository implements AuthorRepository {

	@PersistenceContext
	private final EntityManager em;

	@Override
	public List<Author> findAll() {
		TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
		return query.getResultList();
	}

	@Override
	public Author findById(long id) {
		return em.find(Author.class, id);
	}
}
