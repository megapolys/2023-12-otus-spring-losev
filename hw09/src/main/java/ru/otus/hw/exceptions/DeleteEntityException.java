package ru.otus.hw.exceptions;

public class DeleteEntityException extends RuntimeException {
	public DeleteEntityException(String message) {
		super(message);
	}

	public static DeleteEntityException authorByBooksDependency(long authorId) {
		return new DeleteEntityException("There are books by author with id %d".formatted(authorId));
	}
}
