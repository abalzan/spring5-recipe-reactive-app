package br.com.andrei.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.andrei.services.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {

	private final RecipeService recipeService;

	public IndexController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@RequestMapping({ "", "/", "/index" })
	public String getIndexPage(Model model) {
		log.debug("Getting Index page");

		//now using reactive thymeleaf, we do not need to configure getRecipes like that: 
		//model.addAttribute("recipes", recipeService.getRecipes().collectList().block());
		//this happen because of the webflux dependency that we add in our project.
		//if you take a look on org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class 
		//you will see that the REACTIVE type is getting automatically.  
		model.addAttribute("recipes", recipeService.getRecipes()); 

		return "index";
	}
}
