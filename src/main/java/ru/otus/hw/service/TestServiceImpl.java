package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;
    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        for (Question question : questionDao.findAll()) {
            ioService.printLine(question.text());
            for (int i = 0; i < question.answers().size(); i++) {
                ioService.printFormattedLine("#%d %s", i + 1, question.answers().get(i).text());
            }
            ioService.printLine("");
        }
    }
}
