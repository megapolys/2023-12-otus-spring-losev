package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.model.jpa.Author;
import ru.otus.hw.model.jpa.Book;
import ru.otus.hw.model.jpa.Comment;
import ru.otus.hw.model.jpa.Genre;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JobConfig {
	private static final int CHUNK_SIZE = 10;

	private final Logger logger = LoggerFactory.getLogger("Batch");

	private final AuthorRepository authorRepository;

	private final GenreRepository genreRepository;

	private final BookRepository bookRepository;

	private final CommentRepository commentRepository;

	private final MongoTemplate mongoTemplate;

	private final JobRepository jobRepository;

	private final PlatformTransactionManager platformTransactionManager;

	@StepScope
	@Bean
	public RepositoryItemReader<Author> authorRepositoryItemReader() {
		return new RepositoryItemReaderBuilder<Author>()
			.name("authorReader")
			.sorts(sortingMap())
			.repository(authorRepository)
			.methodName("findAll")
			.pageSize(CHUNK_SIZE)
			.build();
	}

	@StepScope
	@Bean
	public RepositoryItemReader<Genre> genreRepositoryItemReader() {
		return new RepositoryItemReaderBuilder<Genre>()
			.name("genreReader")
			.sorts(sortingMap())
			.repository(genreRepository)
			.methodName("findAll")
			.pageSize(CHUNK_SIZE)
			.build();
	}

	@StepScope
	@Bean
	public RepositoryItemReader<Book> bookRepositoryItemReader() {
		return new RepositoryItemReaderBuilder<Book>()
			.name("bookReader")
			.sorts(sortingMap())
			.repository(bookRepository)
			.methodName("findAll")
			.pageSize(CHUNK_SIZE)
			.build();
	}

	@StepScope
	@Bean
	public RepositoryItemReader<Comment> commentRepositoryItemReader() {
		return new RepositoryItemReaderBuilder<Comment>()
			.name("bookReader")
			.sorts(sortingMap())
			.repository(commentRepository)
			.methodName("findAll")
			.pageSize(CHUNK_SIZE)
			.build();
	}

	private Map<String, Sort.Direction> sortingMap() {
		return Map.of("id", Sort.Direction.ASC);
	}

	@StepScope
	@Bean
	public MongoItemWriter<MongoAuthor> authorWriter() {
		MongoItemWriter<MongoAuthor> writer = new MongoItemWriter<>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection("authors");
		return writer;
	}

	@StepScope
	@Bean
	public MongoItemWriter<MongoGenre> genreWriter() {
		MongoItemWriter<MongoGenre> writer = new MongoItemWriter<>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection("genres");
		return writer;
	}

	@StepScope
	@Bean
	public MongoItemWriter<MongoBook> bookWriter() {
		MongoItemWriter<MongoBook> writer = new MongoItemWriter<>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection("books");
		return writer;
	}

	@StepScope
	@Bean
	public MongoItemWriter<MongoComment> commentWriter() {
		MongoItemWriter<MongoComment> writer = new MongoItemWriter<>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection("comments");
		return writer;
	}

	@Bean
	public Job importFromDatabaseJob(
		Step transformAuthorsStep,
		Step transformGenresStep,
		Step transformBooksStep,
		Step transformCommentsStep
	) {
		return new JobBuilder("importFromDatabaseJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.flow(transformAuthorsStep)
			.next(transformGenresStep)
			.next(transformBooksStep)
			.next(transformCommentsStep)
			.end()
			.listener(new JobExecutionListener() {
				@Override
				public void beforeJob(@NonNull JobExecution jobExecution) {
					logger.info("Начало job");
				}

				@Override
				public void afterJob(@NonNull JobExecution jobExecution) {
					logger.info("Конец job");
				}
			})
			.build();

	}

	@Bean
	public Step transformAuthorsStep(
		RepositoryItemReader<Author> reader,
		MongoItemWriter<MongoAuthor> writer,
		ItemProcessor<Author, MongoAuthor> processor
	) {
		return new StepBuilder("transformAuthorsStep", jobRepository)
			.<Author, MongoAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}

	@Bean
	public Step transformGenresStep(
		RepositoryItemReader<Genre> reader,
		MongoItemWriter<MongoGenre> writer,
		ItemProcessor<Genre, MongoGenre> processor
	) {
		return new StepBuilder("transformGenresStep", jobRepository)
			.<Genre, MongoGenre>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}

	@Bean
	public Step transformBooksStep(
		RepositoryItemReader<Book> reader,
		MongoItemWriter<MongoBook> writer,
		ItemProcessor<Book, MongoBook> processor
	) {
		return new StepBuilder("transformBooksStep", jobRepository)
			.<Book, MongoBook>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}

	@Bean
	public Step transformCommentsStep(
		RepositoryItemReader<Comment> reader,
		MongoItemWriter<MongoComment> writer,
		ItemProcessor<Comment, MongoComment> processor
	) {
		return new StepBuilder("transformCommentsStep", jobRepository)
			.<Comment, MongoComment>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}
}
