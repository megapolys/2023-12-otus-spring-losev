package ru.otus.hw.dao;

import com.opencsv.bean.ColumnPositionMappingStrategyBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

	private final TestFileNameProvider fileNameProvider;

	@Override
	public List<Question> findAll() {
		List<QuestionDto> questions;
		var strategy = new ColumnPositionMappingStrategyBuilder<QuestionDto>().build();
		strategy.setType(QuestionDto.class);
		strategy.setColumnMapping(List.of("text", "answers").toArray(new String[] {}));
		try {
			questions = new CsvToBeanBuilder<QuestionDto>(new BufferedReader(new InputStreamReader(
					new ClassPathResource(fileNameProvider.getTestFileName()).getInputStream()
			)))
					.withMappingStrategy(strategy)
					.withSeparator(';')
					.withSkipLines(1)
					.build()
					.parse();
		} catch (Exception e) {
			throw new QuestionReadException(e.getMessage(), e);
		}
		return questions.stream().map(QuestionDto::toDomainObject).toList();
	}
}
