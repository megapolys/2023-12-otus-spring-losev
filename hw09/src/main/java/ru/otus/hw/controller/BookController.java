package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookFormDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	private final AuthorService authorService;

	private final GenreService genreService;

	@GetMapping("/books")
	public String getBooks(Model model) {
		List<BookDto> books = bookService.findAll();
		model.addAttribute("books", books);
		return "books/list";
	}

	@GetMapping(value = "/books/edit", params = "id")
	public String getBookEdit(@RequestParam("id") long id, Model model) {
		BookDto bookDto = bookService.findById(id);
		model.addAttribute("book", bookDto);
		model.addAttribute("authors", authorService.findAll());
		model.addAttribute("genres", genreService.findAll());
		return "books/edit";
	}

	@PostMapping(value = "/books/edit", params = "id")
	public String editBook(
		@RequestParam("id") long id,
		BookFormDto book,
		RedirectAttributes redirectAttributes,
		Model model
	) {
		try {
			bookService.save(book);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			redirectAttributes.addAttribute("id", id);
			return "redirect:/books/edit?id={id}";
		}
		return "redirect:/books";
	}

	@PostMapping(value = "/books/delete", params = "id")
	public String deleteBook(
		@RequestParam("id") long id,
		RedirectAttributes redirectAttributes
	) {
		try {
			bookService.deleteById(id);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/books";
	}

	@GetMapping(value = "/books/add")
	public String getBookAdd(Model model) {
		model.addAttribute("authors", authorService.findAll());
		model.addAttribute("genres", genreService.findAll());
		return "books/add";
	}

	@PostMapping(value = "/books/add")
	public String addBook(
		BookFormDto book,
		RedirectAttributes redirectAttributes,
		Model model
	) {
		try {
			bookService.save(book);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/books/add";
		}
		return "redirect:/books";
	}
}
