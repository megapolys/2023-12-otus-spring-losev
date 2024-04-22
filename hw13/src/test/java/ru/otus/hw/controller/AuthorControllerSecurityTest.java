package ru.otus.hw.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.otus.hw.config.SpringSecurityConfig;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AuthorController.class)
@Import(SpringSecurityConfig.class)
public class AuthorControllerSecurityTest extends AbstractControllerTest {

	private static Stream<Arguments> urlProvider() {
		String getAll = "/authors";
		String add = "/authors/add";
		String edit = "/authors/edit/1";

		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithoutRoles = user("user").roles();
		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithRoleUser = user("user").roles("USER");
		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithRoleAuthorManager = user("user").roles("AUTHOR_MANAGER");
		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithRoleBookManager = user("user").roles("BOOK_MANAGER");
		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithRoleAdmin = user("user").roles("ADMIN");
		RequestPostProcessor anonymous = SecurityMockMvcRequestPostProcessors.anonymous();

		ResultMatcher redirectStatus = status().is3xxRedirection();
		ResultMatcher forbiddenStatus = status().isForbidden();
		ResultMatcher okStatus = status().isOk();

		return Stream.of(
			Arguments.of(HttpMethod.GET, getAll, anonymous, redirectStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithoutRoles, forbiddenStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithRoleUser, okStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithRoleAuthorManager, forbiddenStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithRoleBookManager, forbiddenStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithRoleAdmin, forbiddenStatus),
			Arguments.of(HttpMethod.GET, add, anonymous, redirectStatus),
			Arguments.of(HttpMethod.GET, add, userWithoutRoles, forbiddenStatus),
			Arguments.of(HttpMethod.GET, add, userWithRoleUser, forbiddenStatus),
			Arguments.of(HttpMethod.GET, add, userWithRoleAuthorManager, okStatus),
			Arguments.of(HttpMethod.GET, add, userWithRoleBookManager, forbiddenStatus),
			Arguments.of(HttpMethod.GET, add, userWithRoleAdmin, okStatus),
			Arguments.of(HttpMethod.GET, edit, anonymous, redirectStatus),
			Arguments.of(HttpMethod.GET, edit, userWithoutRoles, forbiddenStatus),
			Arguments.of(HttpMethod.GET, edit, userWithRoleUser, forbiddenStatus),
			Arguments.of(HttpMethod.GET, edit, userWithRoleAuthorManager, okStatus),
			Arguments.of(HttpMethod.GET, edit, userWithRoleBookManager, forbiddenStatus),
			Arguments.of(HttpMethod.GET, edit, userWithRoleAdmin, okStatus)
		);
	}

	@ParameterizedTest
	@MethodSource("urlProvider")
	void testSecurity(HttpMethod method, String url, RequestPostProcessor user, ResultMatcher resultMatcher) throws Exception {
		mockMvc.perform(request(method, url)
				.with(user))
			.andExpect(resultMatcher);
	}
}
