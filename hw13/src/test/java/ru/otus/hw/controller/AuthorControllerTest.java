package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@ContextConfiguration(classes = AuthorController.class)
class AuthorControllerTest extends AbstractControllerTest {

	@Test
	void whenReturnAuthorsThenSuccess() throws Exception {
		mockMvc.perform(get("/authors"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/list"));
	}

	@Test
	void whenGetAddAuthorThenSuccess() throws Exception {
		mockMvc.perform(get("/authors/add"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/add"));
	}

	@Test
	void whenGetEditAuthorThenSuccess() throws Exception {
		mockMvc.perform(get("/authors/edit/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("authors/edit"))
			.andExpect(model().attribute("id", equalTo(1L)));
	}
}