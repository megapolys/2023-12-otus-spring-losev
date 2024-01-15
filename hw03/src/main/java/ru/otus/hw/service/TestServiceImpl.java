package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

	private final LocalizedIOService ioService;
	private final QuestionDao questionDao;

	@Override
	public TestResult executeTestFor(Student student) {
		ioService.printLine("");
		ioService.printLineLocalized("TestService.answer.the.questions");
		ioService.printLine("");

		var questions = questionDao.findAll();
		var testResult = new TestResult(student);

		for (var question : questions) {
			ioService.printLine(question.text());
			for (int i = 0; i < question.answers().size(); i++) {
				ioService.printFormattedLine("#%d %s", i + 1, question.answers().get(i).text());
			}
			int answer = ioService.readIntForRange(1, question.answers().size(), ioService.getMessage("TestService.illegal.value"));
			var isAnswerValid = question.answers().get(answer - 1).isCorrect();
			testResult.applyAnswer(question, isAnswerValid);
		}
		return testResult;
	}
}
