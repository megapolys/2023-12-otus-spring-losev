package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.jpa.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
