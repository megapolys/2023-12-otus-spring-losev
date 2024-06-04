package com.example.servingwebcontent.config;

import com.example.servingwebcontent.domain.validation.TaskForm;
import com.example.servingwebcontent.domain.validation.TaskType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

	@Bean
	public TaskForm taskForm() {
		final TaskForm taskForm = new TaskForm();
		taskForm.setTaskType(TaskType.FIVE_VARIANT);
		return taskForm;
	}

}
