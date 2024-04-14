package ru.otus.hw.config;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.config.generator.MongoAuthorGenerator;
import ru.otus.hw.config.generator.MongoBookGenerator;
import ru.otus.hw.config.generator.MongoCommentGenerator;
import ru.otus.hw.config.generator.MongoGenreGenerator;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.mongo.MongoGenre;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureDataMongo
@SpringBatchTest
class JobConfigTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@Autowired
	private MongoTemplate mongoTemplate;

	private final static String JOB_NAME = "importFromDatabaseJob";

	@BeforeEach
	void clearMetaData() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	@Test
	void testJob() throws Exception {
		Job job = jobLauncherTestUtils.getJob();
		assertThat(job).isNotNull()
			.extracting(Job::getName)
			.isEqualTo(JOB_NAME);

		JobParameters parameters = new JobParametersBuilder()
			.addDate("date", new Date())
			.toJobParameters();
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);
		assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

		List<MongoAuthor> actualAuthors = mongoTemplate.findAll(MongoAuthor.class);
		List<MongoAuthor> expectedAuthors = MongoAuthorGenerator.generateList();
		equals(actualAuthors, expectedAuthors);

		List<MongoGenre> actualGenres = mongoTemplate.findAll(MongoGenre.class);
		List<MongoGenre> expectedGenres = MongoGenreGenerator.generateList();
		equals(actualGenres, expectedGenres);

		List<MongoBook> actualBooks = mongoTemplate.findAll(MongoBook.class);
		List<MongoBook> expectedBooks = MongoBookGenerator.generateList();
		equals(actualBooks, expectedBooks);

		List<MongoComment> actualComments = mongoTemplate.findAll(MongoComment.class);
		List<MongoComment> expectedComments = MongoCommentGenerator.generateList();
		equals(actualComments, expectedComments);


	}

	private void equals(List<?> actual, List<?> expected) {
		assertThat(actual)
			.isNotEmpty()
			.usingRecursiveComparison()
			.ignoringFieldsMatchingRegexes(".*id")
			.isEqualTo(expected);
	}

}