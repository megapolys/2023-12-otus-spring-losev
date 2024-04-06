package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AuthorController.class)
public class AuthorControllerUnauthorizedTest extends AbstractControllerTest{

	@Test
	void whenReturnAuthorsThenRedirect() throws Exception {
		mockMvc.perform(get("/authors"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	void whenGetAddAuthorThenRedirect() throws Exception {
		mockMvc.perform(get("/authors/add"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	void whenGetEditAuthorThenRedirect() throws Exception {
		mockMvc.perform(get("/authors/edit/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}
}
