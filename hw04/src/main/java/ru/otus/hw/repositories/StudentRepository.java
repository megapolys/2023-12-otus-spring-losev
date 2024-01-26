package ru.otus.hw.repositories;

import ru.otus.hw.domain.Student;

public interface StudentRepository {

	boolean studentExists();

	void putStudent(Student student);

	Student getStudent(String message);
}
