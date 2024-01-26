package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@RequiredArgsConstructor
@ShellComponent
public class TestCommands {

	private final TestRunnerService testRunnerService;

	@ShellMethod(value = "Login student", key = "l")
	public void login() {
		testRunnerService.login();
	}

	@ShellMethod(value = "Testing student", key = "t")
	public void testing() {
		testRunnerService.testing();
	}

	@ShellMethod(value = "Show test result", key = "r")
	public void showResult() {
		testRunnerService.showResult();
	}

}
