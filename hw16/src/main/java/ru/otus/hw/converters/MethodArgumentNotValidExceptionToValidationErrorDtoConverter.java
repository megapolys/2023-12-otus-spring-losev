package ru.otus.hw.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.otus.hw.models.dto.ValidationErrorDto;

@Service
public class MethodArgumentNotValidExceptionToValidationErrorDtoConverter
	implements Converter<MethodArgumentNotValidException, ValidationErrorDto> {
	@Override
	public ValidationErrorDto convert(MethodArgumentNotValidException exception) {
		return new ValidationErrorDto(exception.getBindingResult().getAllErrors().stream()
			.map(error -> new ValidationErrorDto.FieldDto(
				((FieldError) error).getField(),
				error.getDefaultMessage()
			)).toList());
	}
}
