package ru.otus.hw.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.models.dto.ErrorDto;
import ru.otus.hw.models.dto.ValidationErrorDto;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class RestControllerErrorHandler {

	private final ConversionService conversionService;

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException e) {
		log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
		return ResponseEntity.badRequest()
			.body(new ErrorDto(e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<ValidationErrorDto> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e
	) {
		log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
		return ResponseEntity.badRequest()
			.body(conversionService.convert(e, ValidationErrorDto.class));
	}

}
