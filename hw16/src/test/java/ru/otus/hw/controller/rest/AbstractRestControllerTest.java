package ru.otus.hw.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public abstract class AbstractRestControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;
}

