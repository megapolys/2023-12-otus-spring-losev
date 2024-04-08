package ru.otus.hw.exceptions.author;

import lombok.Getter;

@Getter
public class FullNameDuplicateException extends RuntimeException {

	public FullNameDuplicateException(String message) {
		super(message);
	}

	public static FullNameDuplicateException byFullName(String fullName) {
		return new FullNameDuplicateException("Author with same fullName %s already exists".formatted(fullName));
	}
}
