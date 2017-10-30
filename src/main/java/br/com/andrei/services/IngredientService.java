package br.com.andrei.services;

import br.com.andrei.commands.IngredientCommand;

public interface IngredientService {

	IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
	
	IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);
	
	void deleteById(String recipeId, String ingredientId);
}
