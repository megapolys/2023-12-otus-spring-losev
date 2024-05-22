package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChatController {

	@GetMapping("/chat")
	public String getBooks() {
		return "chat";
	}

}
