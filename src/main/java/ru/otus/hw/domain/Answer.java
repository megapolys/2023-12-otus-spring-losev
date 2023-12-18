package ru.otus.hw.domain;

import lombok.Builder;

@Builder
public record Answer(String text, boolean isCorrect) {
}
