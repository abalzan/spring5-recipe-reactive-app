package br.com.andrei.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.andrei.commands.IngredientCommand;
import br.com.andrei.commands.RecipeCommand;
import br.com.andrei.commands.UnitOfMeasureCommand;
import br.com.andrei.services.IngredientService;
import br.com.andrei.services.RecipeService;
import br.com.andrei.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IngredientController {

	private RecipeService recipeService;
	private IngredientService ingredientService;
	private UnitOfMeasureService unitOfMeasureService;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService,
			UnitOfMeasureService unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}

	@GetMapping("recipe/{recipeId}/ingredients")
	public String listIngredients(@PathVariable String recipeId, Model model) {
		log.debug("Getting Ingredients list from recipe: " + recipeId);

		model.addAttribute("recipe", recipeService.findCommandById(recipeId));

		return "recipe/ingredient/list";
	}

	@GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/show")
	public String listIngridients(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
		log.debug("Getting Ingredients list from recipe: " + recipeId);

		model.addAttribute("ingredient",
				ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

		return "recipe/ingredient/show";
	}
	
	@GetMapping("recipe/{recipeId}/ingredient/new")
	public String saveRecipeIngredient(@PathVariable String recipeId, Model model) {

		RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);
		
		IngredientCommand ingredientCommand = new IngredientCommand();
		
		ingredientCommand.setRecipeId(recipeId);
		ingredientCommand.setUom(new UnitOfMeasureCommand());
		
		model.addAttribute("ingredient", ingredientCommand);
		model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
		
		return "recipe/ingredient/ingredientform";
	}

	@GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/update")
	public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {

		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
		model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

		return "recipe/ingredient/ingredientform";
	}

	@PostMapping("recipe/{recipeId}/ingredient")
	public String saveOrUpdateIngredient(@Valid @ModelAttribute IngredientCommand ingredientCommand, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
			
			return "recipe/ingredient/ingredientform";
		}
		
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand);

		log.debug("saved receipe id:" + savedCommand.getRecipeId());
		log.debug("saved ingredient id:" + savedCommand.getId());

		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
	}
	
	@GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/delete")
	public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId) {
	
		ingredientService.deleteById(recipeId, ingredientId);
		
		return "redirect:/recipe/"+ recipeId + "/ingredients";
	}

}
