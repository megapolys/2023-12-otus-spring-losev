package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
@Import({JpaCommentRepository.class})
public class JpaCommentRepositoryTest {

	@Autowired
	private JpaCommentRepository repositoryJpa;

	@Autowired
	private TestEntityManager em;

	@DisplayName("должен загружать комментарий по идентификатору")
	@Test
	void shouldReturnCorrectCommentById() {
		var actualComment = repositoryJpa.findById(1L);
		var expectedComment = em.find(Comment.class, 1L);

		assertThat(actualComment).isEqualTo(expectedComment);
	}

	@DisplayName("должен загружать список всех комментариев по идентификатору книги")
	@Test
	void shouldReturnCorrectCommentsListByBookId() {
		var actualComments = repositoryJpa.findByBookId(1);
		var expectedComments = getDbComments();

		assertThat(actualComments).containsExactlyElementsOf(expectedComments);
		actualComments.forEach(System.out::println);
	}

	private List<Comment> getDbComments() {
		return IntStream.range(1, 3).mapToObj(i -> em.find(Comment.class, i)).toList();
	}
}
