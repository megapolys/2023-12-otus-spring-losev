package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	@EntityGraph("user-role-graph")
	Optional<User> findByUserName(String userName);
}
