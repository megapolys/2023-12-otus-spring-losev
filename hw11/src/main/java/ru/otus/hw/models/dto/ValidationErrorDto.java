package ru.otus.hw.models.dto;

import lombok.Value;

import java.util.List;

@Value
public class ValidationErrorDto {

	private final List<FieldDto> fields;

	@Value
	public static class FieldDto {

		private final String fieldName;

		private final String error;
	}
}
