package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

	boolean existsByFullNameAndIdNot(String fullName, long id);
}
