package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.repositories.TestResultRepository;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

	private final TestConfig testConfig;

	private final LocalizedIOService ioService;

	private final TestResultRepository testResultRepository;

	@Override
	public void showResult() {
		TestResult testResult = testResultRepository.getTestResult(ioService.getMessage("TestResult.not.found"));
		ioService.printLine("");
		ioService.printLineLocalized("ResultService.test.results");
		ioService.printFormattedLineLocalized(
			"ResultService.student",
			testResult.getStudent().getFullName()
		);
		ioService.printFormattedLineLocalized(
			"ResultService.answered.questions.count",
			testResult.getAnsweredQuestions().size()
		);
		ioService.printFormattedLineLocalized(
			"ResultService.right.answers.count",
			testResult.getRightAnswersCount()
		);

		if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
			ioService.printLineLocalized("ResultService.passed.test");
			return;
		}
		ioService.printLineLocalized("ResultService.fail.test");
	}
}
