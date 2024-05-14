package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AuthorController {

	@GetMapping("/authors")
	public String getAuthors() {
		return "authors/list";
	}

	@GetMapping(value = "/authors/edit/{id}")
	public String getAuthorEdit(@PathVariable long id, Model model) {
		model.addAttribute("id", id);
		return "authors/edit";
	}

	@GetMapping("/authors/add")
	public String getAuthorAdd() {
		return "authors/add";
	}
}
