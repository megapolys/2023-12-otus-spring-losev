package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Comment;
import ru.otus.hw.repositories.generator.BookGenerator;
import ru.otus.hw.repositories.generator.CommentGenerator;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями")
@DataMongoTest
public class CommentRepositoryTest {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setup() {
		mongoTemplate.dropCollection("comments");
		mongoTemplate.insert(new Comment("1", "Comment_1", mongoTemplate.findById("1", Book.class)));
		mongoTemplate.insert(new Comment("2", "Comment_2", mongoTemplate.findById("1", Book.class)));
		mongoTemplate.insert(new Comment("3", "Comment_3", mongoTemplate.findById("3", Book.class)));
	}

	@DisplayName("должен загружать комментарий по идентификатору")
	@Test
	void shouldReturnCorrectCommentById() {
		Optional<Comment> actualComment = commentRepository.findById("1");
		Comment expectedComment = CommentGenerator.generate();

		assertThat(actualComment.isPresent()).isTrue();
		assertThat(actualComment.get()).isEqualTo(expectedComment);
	}

	@DisplayName("должен загружать список всех комментариев по идентификатору книги")
	@Test
	void shouldReturnCorrectCommentsListByBookId() {
		List<Comment> actualComments = commentRepository.findByBookId("1");
		List<Comment> expectedComments = CommentGenerator.generateByFirstBook();

		assertThat(actualComments).containsExactlyElementsOf(expectedComments);
		actualComments.forEach(System.out::println);
	}

	@DisplayName("должен сохранять новый комментарий")
	@Test
	void shouldCorrectSaveComment() {
		Book book = BookGenerator.generateFirst();
		Comment expected = commentRepository.save(new Comment("new_comment", book));
		Comment actual = mongoTemplate.findById(expected.getId(), Comment.class);

		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("должен сохранять и получать новый комментарий")
	@Test
	void shouldCorrectSaveAndFindComment() {
		Book book = BookGenerator.generateFirst();
		Comment expected = commentRepository.save(new Comment("new_comment", book));
		Optional<Comment> actual = commentRepository.findById(expected.getId());

		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isEqualTo(expected);
	}

	@DisplayName("должен удалить комментарии по идентификатору книги")
	@Test
	void shouldDeleteCommentByBookId() {
		String bookId = "1";
		commentRepository.deleteAllByBookId(bookId);

		Query query = new Query();
		query.addCriteria(Criteria.where("book.id").is(bookId));
		List<Comment> comments = mongoTemplate.find(query, Comment.class);

		assertThat(comments).isEmpty();
	}
}
