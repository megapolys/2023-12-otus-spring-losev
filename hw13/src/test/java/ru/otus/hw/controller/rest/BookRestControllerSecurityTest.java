package ru.otus.hw.controller.rest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.otus.hw.config.SpringSecurityConfig;
import ru.otus.hw.controller.advice.RestControllerErrorHandler;
import ru.otus.hw.converters.MethodArgumentNotValidExceptionToValidationErrorDtoConverter;
import ru.otus.hw.services.BookService;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
	BookRestController.class,
	RestControllerErrorHandler.class,
	MethodArgumentNotValidExceptionToValidationErrorDtoConverter.class
})
@Import(SpringSecurityConfig.class)
public class BookRestControllerSecurityTest extends AbstractRestControllerTest {

	@MockBean
	private BookService bookService;

	private static Stream<Arguments> urlProvider() {
		String getAll = "/api/v1/books";
		String get = "/api/v1/books/1";
		String post = "/api/v1/books";
		String put = "/api/v1/books/1";
		String delete = "/api/v1/books/1";

		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithoutRoles = user("user").roles();
		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithRoleUser = user("user").roles("USER");
		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithRoleAuthorManager = user("user").roles("AUTHOR_MANAGER");
		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithRoleBookManager = user("user").roles("BOOK_MANAGER");
		SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userWithRoleAdmin = user("user").roles("ADMIN");
		RequestPostProcessor anonymous = SecurityMockMvcRequestPostProcessors.anonymous();

		ResultMatcher redirectStatus = status().is3xxRedirection();
		ResultMatcher forbiddenStatus = status().isForbidden();
		ResultMatcher okStatus = status().isOk();
		ResultMatcher badRequest = status().isBadRequest();

		return Stream.of(
			Arguments.of(HttpMethod.GET, getAll, anonymous, redirectStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithoutRoles, forbiddenStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithRoleUser, okStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithRoleAuthorManager, forbiddenStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithRoleBookManager, forbiddenStatus),
			Arguments.of(HttpMethod.GET, getAll, userWithRoleAdmin, forbiddenStatus),
			Arguments.of(HttpMethod.GET, get, anonymous, redirectStatus),
			Arguments.of(HttpMethod.GET, get, userWithoutRoles, forbiddenStatus),
			Arguments.of(HttpMethod.GET, get, userWithRoleUser, okStatus),
			Arguments.of(HttpMethod.GET, get, userWithRoleAuthorManager, forbiddenStatus),
			Arguments.of(HttpMethod.GET, get, userWithRoleBookManager, forbiddenStatus),
			Arguments.of(HttpMethod.GET, get, userWithRoleAdmin, forbiddenStatus),
			Arguments.of(HttpMethod.POST, post, anonymous, redirectStatus),
			Arguments.of(HttpMethod.POST, post, userWithRoleUser, forbiddenStatus),
			Arguments.of(HttpMethod.POST, post, userWithRoleAuthorManager, forbiddenStatus),
			Arguments.of(HttpMethod.POST, post, userWithRoleBookManager, badRequest),
			Arguments.of(HttpMethod.POST, post, userWithRoleAdmin, badRequest),
			Arguments.of(HttpMethod.PUT, put, anonymous, redirectStatus),
			Arguments.of(HttpMethod.PUT, put, userWithRoleUser, forbiddenStatus),
			Arguments.of(HttpMethod.PUT, put, userWithRoleAuthorManager, forbiddenStatus),
			Arguments.of(HttpMethod.PUT, put, userWithRoleBookManager, badRequest),
			Arguments.of(HttpMethod.PUT, put, userWithRoleAdmin, badRequest),
			Arguments.of(HttpMethod.DELETE, delete, anonymous, redirectStatus),
			Arguments.of(HttpMethod.DELETE, delete, userWithRoleUser, forbiddenStatus),
			Arguments.of(HttpMethod.DELETE, delete, userWithRoleAuthorManager, forbiddenStatus),
			Arguments.of(HttpMethod.DELETE, delete, userWithRoleBookManager, okStatus),
			Arguments.of(HttpMethod.DELETE, delete, userWithRoleAdmin, okStatus)
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
