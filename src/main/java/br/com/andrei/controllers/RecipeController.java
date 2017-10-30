package br.com.andrei.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import br.com.andrei.commands.RecipeCommand;
import br.com.andrei.exceptions.NotFoundException;
import br.com.andrei.services.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RecipeController {

	private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
	
	private final RecipeService recipeService;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	// GetMapping usa a annotation RequestMapping, então ela não se faz necessária.
	@GetMapping("/recipe/{id}/show")
	public String showById(@PathVariable String id, Model model) {

		model.addAttribute("recipe", recipeService.findById(id));

		return "recipe/show";
	}

	@GetMapping("recipe/new")
	public String createRecipe(Model model) {

		model.addAttribute("recipe", new RecipeCommand());

		return RECIPE_RECIPEFORM_URL;
	}

	@GetMapping("recipe/{id}/update")
	public String updateRecipe(@PathVariable String id, Model model) {

		model.addAttribute("recipe", recipeService.findCommandById(id));

		return RECIPE_RECIPEFORM_URL;
	}

	@PostMapping("recipe")
	public String saveOrUpdateRecipe(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
			
			return RECIPE_RECIPEFORM_URL;
		}

		RecipeCommand savedCommand = recipeService.saveRecipeCommand(recipeCommand);

		return "redirect:/recipe/" + savedCommand.getId() + "/show";
	}

	@GetMapping("recipe/{id}/delete")
	public String deleteRecipe(@PathVariable String id) {

		recipeService.deleteById(id);

		return "redirect:/";
	}
}
