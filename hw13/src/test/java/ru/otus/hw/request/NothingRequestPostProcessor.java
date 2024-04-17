package ru.otus.hw.request;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class NothingRequestPostProcessor implements RequestPostProcessor {

	@Override
	public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
		return request;
	}
}
