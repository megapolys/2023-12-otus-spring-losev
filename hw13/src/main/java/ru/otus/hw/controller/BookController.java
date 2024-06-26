package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookController {

	@GetMapping("/books")
	public String getBooks() {
		return "books/list";
	}

	@GetMapping("/books/edit/{id}")
	public String getBookEdit(@PathVariable long id, Model model) {
		model.addAttribute("id", id);
		return "books/edit";
	}

	@GetMapping("/books/add")
	public String getBookAdd() {
		return "books/add";
	}
}
