package ru.otus.hw.service;

import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

public interface TestRunnerService {
	void run();

	Student login();

	TestResult testing(Student student);

	void showResult(TestResult testResult);
}
