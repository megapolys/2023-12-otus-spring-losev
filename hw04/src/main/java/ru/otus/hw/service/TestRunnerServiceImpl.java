package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

	private final TestService testService;

	private final StudentService studentService;

	private final ResultService resultService;

	@Override
	public void run() {
		studentService.determineCurrentStudent();
		testService.executeTestFor();
		resultService.showResult();
	}

	@Override
	public void login() {
		studentService.determineCurrentStudent();
	}

	@Override
	public void testing() {
		testService.executeTestFor();
	}

	@Override
	public void showResult() {
		resultService.showResult();
	}
}
