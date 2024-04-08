package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.AuthorFormDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

	private final AuthorService authorService;

	@GetMapping("/authors")
	public String getAuthors(Model model) {
		List<AuthorDto> authors = authorService.findAll();
		model.addAttribute("authors", authors);
		return "authors/list";
	}

	@GetMapping(value = "/authors/edit/{id}")
	public String getAuthorEdit(@PathVariable("id") long id, Model model) {
		AuthorDto authorDto = authorService.findById(id);
		model.addAttribute("author", authorDto);
		return "authors/edit";
	}

	@PostMapping(value = "/authors/edit")
	public String editAuthor(
		AuthorFormDto author,
		Model model
	) {
		try {
			authorService.save(author);
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("author", author);
			return "authors/edit";
		}
		return "redirect:/authors";
	}

	@GetMapping("/authors/add")
	public String getAuthorAdd() {
		return "authors/add";
	}

	@PostMapping("/authors/add")
	public String addAuthor(
		AuthorFormDto author,
		Model model
	) {
		try {
			authorService.save(author);
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("author", author);
			return "authors/add";
		}
		return "redirect:/authors";
	}

	@PostMapping(value = "/authors/delete/{id}")
	public String deleteAuthor(
		@PathVariable("id") long id,
		RedirectAttributes redirectAttributes
	) {
		try {
			authorService.deleteById(id);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/authors";
	}

}
