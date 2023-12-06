package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.generator.QuestionGenerator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestServiceImplTest {

	private IOService ioService;

	@Mock
	private QuestionDao questionDao;

	private TestServiceImpl testService;
	private StringBuilder outputString;

	@BeforeEach
	void setUp() {
		outputString = new StringBuilder();
		ioService = new IOService() {
			@Override
			public void printLine(String s) {
				outputString.append(s);
			}

			@Override
			public void printFormattedLine(String s, Object... args) {
				outputString.append(String.format(s, args));
			}
		};
		questionDao = mock(QuestionDao.class);
		testService = new TestServiceImpl(ioService, questionDao);
	}

	@Test
	@DisplayName("должен корректно выводить вопросы")
	void printQuestionsWithAnswers() {
		List<Question> questions = QuestionGenerator.all();
		when(questionDao.findAll()).thenReturn(questions);

		testService.executeTest();

		String out = outputString.toString();
		assertThat(out).contains(List.of(questions.get(0).text(), questions.get(0).answers().get(0).text()));
	}

	@Test
	@DisplayName("должен корректно выводить вопросы без ответов")
	void printQuestionsWithNoAnswers() {
		List<Question> questions = QuestionGenerator.nullAnswers();
		when(questionDao.findAll()).thenReturn(questions);

		testService.executeTest();
	}
}