package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

	@GetMapping("/main")
	public String getMain(Principal principal, Model model) {
		if (principal != null) {
			model.addAttribute("userName", principal.getName());
		} else {
			model.addAttribute("userName", "unanimous");
		}
		return "main";
	}

	@GetMapping("/")
	public String getDefault() {
		return "redirect:/main";
	}
}
