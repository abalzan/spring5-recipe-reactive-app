package br.com.andrei.services;

import java.util.Set;

import br.com.andrei.commands.RecipeCommand;
import br.com.andrei.domain.Recipe;


public interface RecipeService {

	Set<Recipe> getRecipes();

	Recipe findById(String id);
	
	void deleteById(String id);

	RecipeCommand findCommandById(String id);
	
	RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
	
}
