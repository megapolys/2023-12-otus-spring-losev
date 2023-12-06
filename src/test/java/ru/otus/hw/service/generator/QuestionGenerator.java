package ru.otus.hw.service.generator;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionGenerator {

	public static List<Question> all() {
		return List.of(
				valid(),
				emptyText()
		);
	}

	public static List<Question> nullAnswers() {
		return List.of(
				emptyAnswersQuestion()
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

	public static Question emptyAnswersQuestion() {
		ArrayList<Answer> answers = new ArrayList<>();
		answers.add(null);
		return Question.builder()
				.text("question 1")
				.answers(answers)
				.build();
	}
}
