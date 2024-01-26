package ru.otus.hw.repositories;

import ru.otus.hw.domain.TestResult;

public interface TestResultRepository {

	void putTestResult(TestResult testResult);

	TestResult getTestResult(String message);
}
