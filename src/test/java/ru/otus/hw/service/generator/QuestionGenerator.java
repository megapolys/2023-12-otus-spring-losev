package ru.otus.hw.service.generator;

import ru.otus.hw.domain.Question;

import java.util.List;

public class QuestionGenerator {

	public static List<Question> all() {
		return List.of(
				valid(),
				emptyText()
		);
	}

	public static Question valid() {
		return Question.builder()
				.text("question 1")
				.answers(AnswerGenerator.all())
				.build();
	}

	public static Question emptyText() {
		return Question.builder()
				.text("")
				.answers(AnswerGenerator.all())
				.build();
	}
}
