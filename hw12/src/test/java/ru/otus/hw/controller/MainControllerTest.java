package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = MainController.class)
public class MainControllerTest extends AbstractControllerTest {

	@Test
	@WithMockUser
	void getMainThenSuccess() throws Exception {
		mockMvc.perform(get("/main"))
			.andExpect(status().isOk())
			.andExpect(view().name("main"))
			.andExpect(model().attribute("userName", "user"));
	}

	@Test
	void getMainWithoutAuthenticatedThenSuccess() throws Exception {
		mockMvc.perform(get("/main"))
			.andExpect(status().isOk())
			.andExpect(view().name("main"))
			.andExpect(model().attribute("userName", "unanimous"));
	}
}

