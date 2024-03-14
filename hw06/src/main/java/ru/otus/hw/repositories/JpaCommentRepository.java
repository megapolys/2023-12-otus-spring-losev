package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.entity.Comment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {

	@PersistenceContext
	private final EntityManager em;

	@Override
	public Comment findById(long id) {
		return em.find(Comment.class, id);
	}

	@Override
	public List<Comment> findByBookId(long bookId) {
		TypedQuery<Comment> query =
			em.createQuery("select c from Comment c where c.book.id = :id", Comment.class);
		query.setParameter("id", bookId);
		return query.getResultList();
	}

	@Override
	public Comment save(Comment comment) {
		if (comment.getId() == 0) {
			em.persist(comment);
			return comment;
		} else {
			return em.merge(comment);
		}
	}

	@Override
	public void deleteById(long id) {
		Comment comment = em.find(Comment.class, id);
		if (comment != null) {
			em.remove(comment);
		} else {
			throw new EntityNotFoundException("Comment with id %d not found".formatted(id));
		}
	}
}
