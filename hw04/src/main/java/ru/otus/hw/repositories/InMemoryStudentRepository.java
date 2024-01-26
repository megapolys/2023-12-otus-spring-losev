package ru.otus.hw.repositories;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.StudentNotFoundException;

@Service
public class InMemoryStudentRepository implements StudentRepository {

	private Student student;


	@Override
	public boolean studentExists() {
		return student != null;
	}

	@Override
	public void putStudent(Student student) {
		this.student = student;
	}

	@Override
	public Student getStudent(String message) {
		if (this.student != null) {
			return this.student;
		} else {
			throw new StudentNotFoundException(message);
		}
	}
}
