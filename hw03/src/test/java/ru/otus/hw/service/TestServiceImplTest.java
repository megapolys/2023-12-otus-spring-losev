package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.generator.question.QuestionGenerator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class TestServiceImplTest {

	@MockBean
	private LocalizedIOService localizedIOService;

	@MockBean
	private QuestionDao questionDao;

	@Autowired
	private TestServiceImpl testService;

	private StringBuilder outputString;

	@BeforeEach
	void setUp() {
		outputString = new StringBuilder();
		doAnswer(invocationOnMock -> {
			outputString.append((String) invocationOnMock.getArgument(0));
			return null;
		}).when(localizedIOService).printLine(anyString());
		doAnswer(invocationOnMock -> {
			outputString.append(String.format((String) invocationOnMock.getArgument(0),
											  invocationOnMock.getArgument(1),
											  invocationOnMock.getArgument(2)
			));
			return null;
		}).when(localizedIOService).printFormattedLine(anyString(), any(), any());
		doAnswer(invocationOnMock -> 1).when(localizedIOService).readIntForRange(anyInt(), anyInt(), anyString());
		testService = new TestServiceImpl(localizedIOService, questionDao);
	}

	@Test
	@DisplayName("должен корректно выводить вопросы")
	void printQuestionsWithAnswers() {
		List<Question> questions = QuestionGenerator.listOfValidAndEmptyQuestions();
		Student student = new Student("Иван", "Иванов");

		when(questionDao.findAll()).thenReturn(questions);

		testService.executeTestFor(student);

		String out = outputString.toString();
		assertThat(out).contains(List.of(questions.get(0).text(), questions.get(0).answers().get(0).text()));
	}
}