package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Date;

@ShellComponent
@RequiredArgsConstructor
public class BatchCommands {

	private final JobLauncher jobLauncher;

	private final Job importJob;

	@ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm-jl")
	public void startMigrationJobWithJobLauncher() throws Exception {
		JobExecution execution = jobLauncher.run(importJob, new JobParametersBuilder()
			.addDate("date", new Date())
			.toJobParameters());
		System.out.println(execution);
	}
}
