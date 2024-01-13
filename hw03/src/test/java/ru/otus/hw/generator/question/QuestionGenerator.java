package ru.otus.hw.generator.question;

import ru.otus.hw.domain.Question;

import java.util.List;

public class QuestionGenerator {

	public static List<Question> listOfValidAndEmptyQuestions() {
		return List.of(
			validQuestion("question 1"),
			emptyQuestion()
		);
	}

	public static List<Question> listOfValidQuestions() {
		return List.of(
			validQuestion("question 1"),
			validQuestion("question 2")
		);
	}

	public static Question validQuestion(String text) {
		return Question.builder()
			.text(text)
			.answers(AnswerGenerator.all())
			.build();
	}

	public static Question emptyQuestion() {
		return Question.builder()
			.text("")
			.answers(AnswerGenerator.all())
			.build();
	}
}