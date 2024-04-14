package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.IdTransform;

import java.util.Optional;

public interface IdTransformRepository extends MongoRepository<IdTransform, String> {

	Optional<IdTransform> findByTableNameAndOldId(String tableName, long oldId);
}
