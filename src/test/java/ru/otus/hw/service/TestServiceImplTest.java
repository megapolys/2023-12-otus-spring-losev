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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TestServiceImplTest {

	private IOService ioService;

	@Mock
	private QuestionDao questionDao;

	private TestServiceImpl testService;
	private StringBuilder outputString;

	@BeforeEach
	void setUp() {
		outputString = new StringBuilder();
		ioService = mock(IOService.class);
		doAnswer(invocationOnMock -> {
			outputString.append((String) invocationOnMock.getArgument(0));
			return null;
		}).when(ioService).printLine(anyString());
		doAnswer(invocationOnMock -> {
			outputString.append(String.format((String) invocationOnMock.getArgument(0), invocationOnMock.getArgument(1), invocationOnMock.getArgument(2)));
			return null;
		}).when(ioService).printFormattedLine(anyString(), any(), any());
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
}