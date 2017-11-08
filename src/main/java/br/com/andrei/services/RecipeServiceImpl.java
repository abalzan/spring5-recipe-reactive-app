package br.com.andrei.services;

import org.springframework.stereotype.Service;

import br.com.andrei.commands.RecipeCommand;
import br.com.andrei.converters.RecipeCommandToRecipe;
import br.com.andrei.converters.RecipeToRecipeCommand;
import br.com.andrei.domain.Recipe;
import br.com.andrei.repositories.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

	private final RecipeReactiveRepository recipeReactiveRepository;
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;

	public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository, RecipeCommandToRecipe recipeCommandToRecipe,
			RecipeToRecipeCommand recipeToRecipeCommand) {
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}

	@Override
	public Flux<Recipe> getRecipes() {
		log.debug("Recipe Service "+ getClass().getName() );

		return recipeReactiveRepository.findAll();
	}

	@Override
	public Mono<Recipe> findById(String id) {
		log.debug("Recipe Service "+ getClass().getName() );
		
		return recipeReactiveRepository.findById(id);
	}

	@Override
	public Mono<RecipeCommand> findCommandById(String id) {
		log.debug("Recipe Service "+ getClass().getName() );
//		return recipeReactiveRepository.findById(id).map(recipeToRecipeCommand::convert);
		return recipeReactiveRepository.findById(id)
				.map(recipe -> {
					RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
					recipeCommand.getIngredients().forEach(rc -> rc.setRecipeId(recipeCommand.getId()));
					
					return recipeCommand;
				});
	}
	
	@Override
	public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {

		log.debug("Recipe Service "+ getClass().getName() );
//		Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
//		Recipe savedRecipe = recipeReactiveRepository.save(detachedRecipe).block();
//		RecipeCommand recipeCommandSaved = recipeToRecipeCommand.convert(savedRecipe);
		return recipeReactiveRepository.save(recipeCommandToRecipe.convert(recipeCommand)).map(recipeToRecipeCommand::convert);
	}

	@Override
	public Mono<Void> deleteById(String id) {
		log.debug("Delete Recipe Id " + id);
		recipeReactiveRepository.deleteById(id).block();
		
		return Mono.empty();
	}
}
