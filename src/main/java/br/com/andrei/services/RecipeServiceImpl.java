package br.com.andrei.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.andrei.commands.RecipeCommand;
import br.com.andrei.converters.RecipeCommandToRecipe;
import br.com.andrei.converters.RecipeToRecipeCommand;
import br.com.andrei.domain.Recipe;
import br.com.andrei.exceptions.NotFoundException;
import br.com.andrei.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

	private final RecipeRepository recipeRepository;
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;

	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe,
			RecipeToRecipeCommand recipeToRecipeCommand) {
		this.recipeRepository = recipeRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}

	@Override
	public Set<Recipe> getRecipes() {
		log.debug("I'm in the service");

		Set<Recipe> recipeSet = new HashSet<>();
		recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
		return recipeSet;
	}

	@Override
	public Recipe findById(String id) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(id);

		if (!recipeOptional.isPresent()) {
			throw new NotFoundException("Recipe Not Found. For ID value: "+id );
		}

		return recipeOptional.get();
	}

	@Override
	public RecipeCommand findCommandById(String id) {
		return recipeToRecipeCommand.convert(findById(id));
	}
	
	@Override
	public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
		Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);

		Recipe savedRecipe = recipeRepository.save(detachedRecipe);
		log.debug("Saved Recipe Id " + savedRecipe.getId());
		return recipeToRecipeCommand.convert(savedRecipe);
	}

	@Override
	public void deleteById(String id) {
		log.debug("Delete Recipe Id " + id);
		recipeRepository.deleteById(id);
	}
}
