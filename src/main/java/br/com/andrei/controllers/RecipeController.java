package br.com.andrei.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.andrei.commands.RecipeCommand;
import br.com.andrei.services.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RecipeController {

	private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
	
	private final RecipeService recipeService;
	
	private WebDataBinder webDataBinder;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		this.webDataBinder = webDataBinder;
	}

	@GetMapping("/recipe/{id}/show")
	public String showById(@PathVariable String id, Model model) {
		log.error("Showing recipeID: " + id);
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
		
		model.addAttribute("recipe", recipeService.findCommandById(id).block());

		return RECIPE_RECIPEFORM_URL;
	}

	@PostMapping("recipe")
	public String saveOrUpdateRecipe(@ModelAttribute("recipe") RecipeCommand recipeCommand) {

		webDataBinder.validate();
		
		BindingResult bindingResult = webDataBinder.getBindingResult();
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
			
			return RECIPE_RECIPEFORM_URL;
		}

		RecipeCommand savedCommand = recipeService.saveRecipeCommand(recipeCommand).block();

		return "redirect:/recipe/" + savedCommand.getId() + "/show";
	}

	@GetMapping("recipe/{id}/delete")
	public String deleteRecipe(@PathVariable String id) {

		recipeService.deleteById(id);

		return "redirect:/";
	}
}
