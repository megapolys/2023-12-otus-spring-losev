package ru.otus.hw.service.generator;

import org.mockito.internal.matchers.And;
import ru.otus.hw.domain.Answer;

import java.util.List;

public class AnswerGenerator {

	public static List<Answer> all() {
		return List.of(
				valid(),
				emptyText()
		);
	}

	public static Answer valid() {
		return Answer.builder()
				.text("valid answer")
				.isCorrect(true)
				.build();
	}

	public static Answer emptyText() {
		return Answer.builder()
				.text("")
				.isCorrect(true)
				.build();
	}
}
