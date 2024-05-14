package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Comment;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями")
@DataJpaTest
public class JpaCommentRepositoryTest {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private TestEntityManager em;

	@DisplayName("должен загружать комментарий по идентификатору")
	@Test
	void shouldReturnCorrectCommentById() {
		var actualComment = commentRepository.findById(1L);
		var expectedComment = em.find(Comment.class, 1L);

		assertThat(actualComment.isPresent()).isTrue();
		assertThat(actualComment.get()).isEqualTo(expectedComment);
	}

	@DisplayName("должен загружать список всех комментариев по идентификатору книги")
	@Test
	void shouldReturnCorrectCommentsListByBookId() {
		var actualComments = commentRepository.findByBookId(1);
		var expectedComments = getDbComments();

		assertThat(actualComments).containsExactlyElementsOf(expectedComments);
		actualComments.forEach(System.out::println);
	}

	private List<Comment> getDbComments() {
		return IntStream.range(1, 3).mapToObj(i -> em.find(Comment.class, i)).toList();
	}

	@DisplayName("должен сохранять новый комментарий")
	@Test
	void shouldCorrectSaveComment() {
		Book book = em.find(Book.class, 1);
		Comment expected = commentRepository.save(new Comment(0, "new_comment", book));

		Comment actual = em.find(Comment.class, expected.getId());
		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("должен сохранять и получать новый комментарий")
	@Test
	void shouldCorrectSaveAndFindComment() {
		Book book = em.find(Book.class, 1);
		Comment expected = commentRepository.save(new Comment(0, "new_comment", book));
		Optional<Comment> actual = commentRepository.findById(expected.getId());

		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isEqualTo(expected);
	}
}
