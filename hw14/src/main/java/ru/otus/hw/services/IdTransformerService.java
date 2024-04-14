package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.IdTransform;
import ru.otus.hw.repositories.IdTransformRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdTransformerService {

	private final IdTransformRepository idTransformRepository;

	public String saveId(String tableName, long oldId) {
		Optional<IdTransform> foundIdTransform = idTransformRepository.findByTableNameAndOldId(tableName, oldId);
		if (foundIdTransform.isPresent()) {
			return foundIdTransform.get().getId();
		}
		return idTransformRepository.save(new IdTransform(tableName, oldId)).getId();
	}

}
