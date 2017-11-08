package br.com.andrei.services;

import br.com.andrei.commands.RecipeCommand;
import br.com.andrei.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface RecipeService {

	Flux<Recipe> getRecipes();

	Mono<Recipe> findById(String id);
	
	Mono<Void> deleteById(String id);

	Mono<RecipeCommand> findCommandById(String id);
	
	Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand);
	
}
