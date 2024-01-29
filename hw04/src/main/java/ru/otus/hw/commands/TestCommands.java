package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.StudentNotFoundException;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@RequiredArgsConstructor
@ShellComponent
public class TestCommands {

	private final TestRunnerService testRunnerService;

	private final LocalizedIOService ioService;

	private Student student;

	private TestResult testResult;

	@ShellMethod(value = "Login student", key = "l")
	public void login() {
		if (student != null) {
			throw new StudentNotFoundException(ioService.getMessage("Student.already.login"));
		}
		student = testRunnerService.login();
	}

	@ShellMethod(value = "Testing student", key = "t")
	public void testing() {
		if (student == null) {
			throw new StudentNotFoundException(ioService.getMessage("Student.didnt.login"));
		}
		testResult = testRunnerService.testing(student);
	}

	@ShellMethod(value = "Show test result", key = "r")
	public void showResult() {
		if (testResult == null) {
			throw new StudentNotFoundException(ioService.getMessage("TestResult.not.found"));
		}
		testRunnerService.showResult(testResult);
	}

}
