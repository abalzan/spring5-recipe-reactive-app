package br.com.andrei.services;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import br.com.andrei.commands.IngredientCommand;
import br.com.andrei.converters.IngredientCommandToIngredient;
import br.com.andrei.converters.IngredientToIngredientCommand;
import br.com.andrei.domain.Ingredient;
import br.com.andrei.domain.Recipe;
import br.com.andrei.repositories.RecipeReactiveRepository;
import br.com.andrei.repositories.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final RecipeReactiveRepository recipeReactiveRepository;
	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;

	public IngredientServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
			IngredientToIngredientCommand ingredientToIngredientCommand,
			UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
			IngredientCommandToIngredient ingredientCommandToIngredient) {
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
	}

	@Override
	public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		return recipeReactiveRepository.findById(recipeId)
				.flatMapIterable(Recipe::getIngredients)
				.filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
				.single()
				.map(ingredient -> {
					IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
					command.setRecipeId(recipeId);
					return command;
				});
	}

	@Override
	public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand) {

		Recipe recipe = recipeReactiveRepository.findById(ingredientCommand.getRecipeId()).block();

		if (recipe == null) {
			log.error("No Recipe Found for id: " + ingredientCommand.getId());
			return Mono.just(new IngredientCommand());
		} else {
			Optional<Ingredient> ingredientOptional = recipe.getIngredients()
					.stream()
					.filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientCommand.getId()))
					.findFirst();

			if (ingredientOptional.isPresent()) {
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(ingredientCommand.getDescription());
				ingredientFound.setAmount(ingredientCommand.getAmount());
				ingredientFound.setUom(unitOfMeasureReactiveRepository.findById(ingredientCommand.getUom().getId()).block());
				if(ingredientCommand.getUom() == null) {
					new RuntimeException("Unit of measure not found");
				}
			} else {
				Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
				ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);
			}

			Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

			Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
					.filter(recipeIngredients -> recipeIngredients.getId().equals(ingredientCommand.getId()))
					.findFirst();

			if (!savedIngredientOptional.isPresent()) {
				savedIngredientOptional = savedRecipe.getIngredients().stream()
						.filter(recipeIngredients -> recipeIngredients.getDescription()
								.equalsIgnoreCase(ingredientCommand.getDescription()))
						.filter(recipeIngredients -> recipeIngredients.getAmount()
								.equals(ingredientCommand.getAmount()))
						.filter(recipeIngredients -> recipeIngredients.getUom().getId()
								.equals(ingredientCommand.getUom().getId()))
						.findFirst();
			}

			IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
			ingredientCommandSaved.setRecipeId(recipe.getId());

			return Mono.just(ingredientCommandSaved);
		}

	}

	@Override
	public Mono<Void> deleteById(String recipeId, String ingredientId) {
		log.debug("Delete Ingredient Id " + ingredientId);
		Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

		if (recipe != null) {
			Optional<Ingredient> ingredientOptional = recipe.getIngredients()
					.stream()
					.filter(ingredient -> ingredient.getId().equals(ingredientId))
					.findFirst();

			if (ingredientOptional.isPresent()) {
				log.debug("Ingredient Found");
				
				recipe.getIngredients().remove(ingredientOptional.get());
				recipeReactiveRepository.save(recipe).block();
			}
		} else {
			log.debug("Recipe Id not found: " + recipeId);
		}
		return Mono.empty();
	}

}
