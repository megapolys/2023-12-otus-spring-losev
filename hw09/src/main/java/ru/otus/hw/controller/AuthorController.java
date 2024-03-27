package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.models.dto.AuthorDto;
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

	@GetMapping(value = "/authors/edit", params = "id")
	public String getAuthorEdit(@RequestParam("id") long id, Model model) {
		AuthorDto authorDto = authorService.findById(id);
		model.addAttribute("author", authorDto);
		return "authors/edit";
	}

	@PostMapping(value = "/authors/edit")
	public String editAuthor(
		@Valid @ModelAttribute("author") AuthorDto authorDto,
		BindingResult bindingResult,
		Model model
	) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("author", authorDto);
			return "authors/edit";
		}
		try {
			authorService.save(authorDto);
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("author", authorDto);
			return "authors/edit";
		}
		return "redirect:/authors";
	}

	@PostMapping(value = "/authors/delete", params = "id")
	public String deleteAuthor(
		@RequestParam("id") long id,
		RedirectAttributes redirectAttributes
	) {
		try {
			authorService.deleteById(id);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/authors";
	}

	@GetMapping("/authors/add")
	public String getAuthorAdd(Model model) {
		return "authors/add";
	}

	@PostMapping("/authors/add")
	public String addAuthor(
		@Valid @ModelAttribute("author") AuthorDto authorDto,
		BindingResult bindingResult,
		Model model
	) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("author", authorDto);
			return "authors/add";
		}
		try {
			authorService.save(authorDto);
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("author", authorDto);
			return "authors/add";
		}
		return "redirect:/authors";
	}

}
