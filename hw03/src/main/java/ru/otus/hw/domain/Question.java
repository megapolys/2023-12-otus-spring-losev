package ru.otus.hw.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record Question(String text, List<Answer> answers) {
}