package ru.otus.hw.repositories;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.TestResultNotFoundException;

@Service
public class InMemoryTestResultRepository implements TestResultRepository {

	private TestResult testResult;

	@Override
	public void putTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	@Override
	public TestResult getTestResult(String message) {
		if (testResult == null) {
			throw new TestResultNotFoundException(message);
		} else {
			return testResult;
		}
	}
}
