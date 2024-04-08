package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AuthorController.class)
public class AuthorControllerSecurityTest extends AbstractControllerTest{

	@Test
	void whenReturnAuthorsThenRedirect() throws Exception {
		mockMvc.perform(get("/authors"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser(roles = {})
	void whenReturnAuthorsThenForbidden() throws Exception {
		mockMvc.perform(get("/authors"))
			.andExpect(status().isForbidden());
	}

	@Test
	void whenGetAddAuthorThenRedirect() throws Exception {
		mockMvc.perform(get("/authors/add"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser
	void whenGetAddAuthorThenForbidden() throws Exception {
		mockMvc.perform(get("/authors/add"))
			.andExpect(status().isForbidden());
	}

	@Test
	void whenGetEditAuthorThenRedirect() throws Exception {
		mockMvc.perform(get("/authors/edit/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser
	void whenGetEditAuthorThenForbidden() throws Exception {
		mockMvc.perform(get("/authors/edit/1"))
			.andExpect(status().isForbidden());
	}
}
