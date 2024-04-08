package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AuthorController.class)
public class BookControllerSecurityTest extends AbstractControllerTest {

	@Test
	void whenReturnBooksThenRedirect() throws Exception {
		mockMvc.perform(get("/books"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser(roles = {})
	void whenReturnBooksThenForbidden() throws Exception {
		mockMvc.perform(get("/books"))
			.andExpect(status().isForbidden());
	}

	@Test
	void whenGetAddBookThenRedirect() throws Exception {
		mockMvc.perform(get("/books/add"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser
	void whenGetAddBookThenForbidden() throws Exception {
		mockMvc.perform(get("/books/add"))
			.andExpect(status().isForbidden());
	}

	@Test
	void whenGetEditBookThenRedirect() throws Exception {
		mockMvc.perform(get("/books/edit/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/authentication/login"));
	}

	@Test
	@WithMockUser
	void whenGetEditBookThenForbidden() throws Exception {
		mockMvc.perform(get("/books/edit/1"))
			.andExpect(status().isForbidden());
	}
}
