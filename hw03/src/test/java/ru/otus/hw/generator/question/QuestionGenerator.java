package ru.otus.hw.generator.question;

import ru.otus.hw.domain.Question;

import java.util.List;

public class QuestionGenerator {

	public static List<Question> all() {
		return List.of(
				valid("question 1"),
				emptyText()
		);
	}

	public static List<Question> findAll() {
		return List.of(
			valid("question 1"),
			valid("question 2")
		);
	}

	public static Question valid(String text) {
		return Question.builder()
				.text(text)
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