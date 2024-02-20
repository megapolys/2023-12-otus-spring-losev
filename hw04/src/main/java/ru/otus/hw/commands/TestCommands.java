package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
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
	@ShellMethodAvailability("loginAvailabilityCheck")
	public void login() {
		student = testRunnerService.login();
	}

	@ShellMethod(value = "Testing student", key = "t")
	@ShellMethodAvailability("testingAvailabilityCheck")
	public void testing() {
		testResult = testRunnerService.testing(student);
	}

	@ShellMethod(value = "Show test result", key = "r")
	@ShellMethodAvailability("showResultAvailabilityCheck")
	public void showResult() {
		testRunnerService.showResult(testResult);
	}

	public Availability loginAvailabilityCheck() {
		return student == null
			? Availability.available()
			: Availability.unavailable(ioService.getMessage("Student.already.login"));
	}

	public Availability testingAvailabilityCheck() {
		return student != null
			? Availability.available()
			: Availability.unavailable(ioService.getMessage("Student.didnt.login"));
	}

	public Availability showResultAvailabilityCheck() {
		return testResult != null
			? Availability.available()
			: Availability.unavailable(ioService.getMessage("TestResult.not.found"));
	}

}
