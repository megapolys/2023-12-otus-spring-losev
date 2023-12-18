package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.generator.question.QuestionGenerator;
import ru.otus.hw.service.LocalizedMessagesServiceImpl;
import ru.otus.hw.service.ResultService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CsvQuestionDaoTest {

	@MockBean
	private LocalizedMessagesServiceImpl localizedMessagesService;

	@MockBean
	private ResultService resultService;

	@Autowired
	private CsvQuestionDao csvQuestionDao;

	@MockBean
	private TestFileNameProvider testFileNameProvider;

	@DisplayName("должен корректно читать вопросы из ресурсов")
	@Test
	void  findAllFromQuestion() throws Exception {
		List<Question> questionsExpected = QuestionGenerator.findAll();
		when(testFileNameProvider.getTestFileName()).thenReturn("questions.csv");

		List<Question> questionsActual = csvQuestionDao.findAll();

		assertEquals(questionsExpected, questionsActual);
	}

}
