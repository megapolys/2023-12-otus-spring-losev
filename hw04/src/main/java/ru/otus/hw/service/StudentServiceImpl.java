package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.StudentAlreadyExistsException;
import ru.otus.hw.repositories.StudentRepository;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final LocalizedIOService ioService;

	private final StudentRepository studentRepository;

	@Override
	public void determineCurrentStudent() {
		if (studentRepository.studentExists()) {
			throw new StudentAlreadyExistsException(ioService.getMessage("Student.already.login"));
		}
		var firstName = ioService.readStringWithPromptLocalized("StudentService.input.first.name");
		var lastName = ioService.readStringWithPromptLocalized("StudentService.input.last.name");
		Student student = new Student(firstName, lastName);
		studentRepository.putStudent(student);
	}
}
