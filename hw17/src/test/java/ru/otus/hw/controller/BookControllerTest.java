package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = BookController.class)
class BookControllerTest extends AbstractControllerTest {

	@Test
	void whenReturnBooksThenSuccess() throws Exception {
		mockMvc.perform(get("/books"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/list"));
	}

	@Test
	void whenGetEditBookThenSuccess() throws Exception {
		mockMvc.perform(get("/books/edit/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/edit"))
			.andExpect(model().attribute("id", equalTo(1L)));
	}

	@Test
	void whenGetAddBookThenSuccess() throws Exception {
		mockMvc.perform(get("/books/add"))
			.andExpect(status().isOk())
			.andExpect(view().name("books/add"));
	}
}