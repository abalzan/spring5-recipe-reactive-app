package br.com.andrei.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.andrei.commands.IngredientCommand;
import br.com.andrei.converters.IngredientCommandToIngredient;
import br.com.andrei.converters.IngredientToIngredientCommand;
import br.com.andrei.domain.Ingredient;
import br.com.andrei.domain.Recipe;
import br.com.andrei.repositories.RecipeRepository;
import br.com.andrei.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final RecipeRepository recipeRepository;
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final UnitOfMeasureRepository unitOfMeasureRepository;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;

	
	public IngredientServiceImpl(RecipeRepository recipeRepository,
			IngredientToIngredientCommand ingredientToIngredientCommand,
			UnitOfMeasureRepository unitOfMeasureRepository,
			IngredientCommandToIngredient ingredientCommandToIngredient) {
		this.recipeRepository = recipeRepository;
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.unitOfMeasureRepository = unitOfMeasureRepository;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

		if (!recipeOptional.isPresent()) {
			log.error("No Recipe Found");
		}

		Recipe recipe = recipeOptional.get();

		Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(ingredientId))
				.map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

		if (!ingredientCommandOptional.isPresent()) {
			log.error("No Ingredient Found");
		}

		return ingredientCommandOptional.get();
	}

	@Override
	public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

		if (!recipeOptional.isPresent()) {
			log.error("No Recipe Found for id: " + ingredientCommand.getId());
			return new IngredientCommand();
		} else {
			Recipe recipe = recipeOptional.get();

			Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
					.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId())).findFirst();

			if (ingredientOptional.isPresent()) {
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(ingredientCommand.getDescription());
				ingredientFound.setAmount(ingredientCommand.getAmount());
				ingredientFound.setUom(unitOfMeasureRepository.findById(ingredientCommand.getUom().getId())
						.orElseThrow(() -> new RuntimeException("Unit of Measure not Found!!!")));
			} else {
				Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
				ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);
			}

			Recipe savedRecipe = recipeRepository.save(recipe);

			Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
					.filter(recipeIngredients -> recipeIngredients.getId().equals(ingredientCommand.getId()))
					.findFirst();
			
			if(!savedIngredientOptional.isPresent()) {
				savedIngredientOptional = savedRecipe.getIngredients().stream()
						.filter(recipeIngredients -> recipeIngredients.getDescription()	.equals(ingredientCommand.getDescription()))
						.filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
						.filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(ingredientCommand.getUom().getId()))
						.findFirst();
			}
			
			IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
			ingredientCommandSaved.setRecipeId(recipe.getId());
			
			return ingredientCommandSaved;

		}

	}
	
	@Override
	public void deleteById(String recipeId, String ingredientId) {
		log.debug("Delete Ingredient Id " + ingredientId);
		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		
		if(recipeOptional.isPresent()) {
			Recipe recipe = recipeOptional.get();
			
			Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
					.filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst();
		
			if(ingredientOptional.isPresent()) {
				log.debug("Ingredient Found");
				Ingredient ingredientToDelete = ingredientOptional.get();
				ingredientToDelete.setRecipe(null);
				recipe.getIngredients().remove(ingredientOptional.get());
				recipeRepository.save(recipe);
			}
		} else {
			log.debug("Recipe Id not found: "+ recipeId);
		}
	}

}
