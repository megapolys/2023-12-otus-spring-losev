package ru.otus.hw.generator.question;

import ru.otus.hw.domain.Answer;

import java.util.List;

public class AnswerGenerator {

	public static List<Answer> listOfAnswers() {
		return List.of(
			validTrue(),
			validFalse()
		);
	}

	public static Answer validTrue() {
		return Answer.builder()
			.text("valid true answer")
			.isCorrect(true)
			.build();
	}

	public static Answer validFalse() {
		return Answer.builder()
			.text("valid false answer")
			.isCorrect(false)
			.build();
	}
}