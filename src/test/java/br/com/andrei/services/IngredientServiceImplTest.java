package br.com.andrei.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.andrei.commands.IngredientCommand;
import br.com.andrei.commands.UnitOfMeasureCommand;
import br.com.andrei.converters.IngredientCommandToIngredient;
import br.com.andrei.converters.IngredientToIngredientCommand;
import br.com.andrei.converters.UnitOfMeasureCommandToUnitOfMeasure;
import br.com.andrei.converters.UnitOfMeasureToUnitOfMeasureCommand;
import br.com.andrei.domain.Ingredient;
import br.com.andrei.domain.Recipe;
import br.com.andrei.domain.UnitOfMeasure;
import br.com.andrei.repositories.RecipeReactiveRepository;
import br.com.andrei.repositories.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Mono;

public class IngredientServiceImplTest {

	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;

	@Mock
	RecipeReactiveRepository recipeReactiveRepository;

	@Mock
	UnitOfMeasureReactiveRepository unitOfMeasureRepository;

	IngredientService ingredientService;

	// init converters
	public IngredientServiceImplTest() {
		this.ingredientToIngredientCommand = new IngredientToIngredientCommand(
				new UnitOfMeasureToUnitOfMeasureCommand());
		this.ingredientCommandToIngredient = new IngredientCommandToIngredient(
				new UnitOfMeasureCommandToUnitOfMeasure());
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		ingredientService = new IngredientServiceImpl(recipeReactiveRepository, ingredientToIngredientCommand,
				unitOfMeasureRepository, ingredientCommandToIngredient);
	}

	@Test
	public void findByRecipeIdAndReceipeIdHappyPath() throws Exception {
		// given
		Recipe recipe = new Recipe();
		recipe.setId("1");

		Ingredient ingredient1 = new Ingredient();
		ingredient1.setId("1");

		Ingredient ingredient2 = new Ingredient();
		ingredient2.setId("1");

		Ingredient ingredient3 = new Ingredient();
		ingredient3.setId("3");

		recipe.addIngredient(ingredient1);
		recipe.addIngredient(ingredient2);
		recipe.addIngredient(ingredient3);

		when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

		// then
		IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("1", "3").block();

		// when
		assertEquals("3", ingredientCommand.getId());
		// assertEquals("1", ingredientCommand.getRecipeId());
		verify(recipeReactiveRepository, times(1)).findById(anyString());
	}

	@Test(expected= RuntimeException.class)
	public void findByRecipeIdAndIdNoRecipeFound() throws Exception {
		// given
		Recipe recipe = new Recipe();
		recipe.setId("1");

		Ingredient ingredient1 = new Ingredient();
		ingredient1.setId("1");

		Ingredient ingredient2 = new Ingredient();
		ingredient2.setId("2");

		Ingredient ingredient3 = new Ingredient();
		ingredient3.setId("3");

		// then
		ingredientService.findByRecipeIdAndIngredientId("2", "3").block();
	}

	@Test(expected= NoSuchElementException.class)
	public void findByRecipeIdAndReceipeIdNoIngredientFound() throws Exception {
		// given
		Recipe recipe = new Recipe();
		recipe.setId("1");

		Ingredient ingredient1 = new Ingredient();
		ingredient1.setId("1");

		Ingredient ingredient2 = new Ingredient();
		ingredient2.setId("1");

		Ingredient ingredient3 = new Ingredient();
		ingredient3.setId("3");

		recipe.addIngredient(ingredient1);
		recipe.addIngredient(ingredient2);
		recipe.addIngredient(ingredient3);

		when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

		// then
		ingredientService.findByRecipeIdAndIngredientId("1", "4").block();

	}

	@Test
	public void testSaveRecipeCommand() throws Exception {
		// given
			
		IngredientCommand ingredientCommand = new IngredientCommand();
		ingredientCommand.setId("3");
		ingredientCommand.setRecipeId("2");
		ingredientCommand.setUom(new UnitOfMeasureCommand());
		ingredientCommand.getUom().setId("1234");

		Recipe savedRecipe = new Recipe();
//		savedRecipe.setId(ingredientCommand.getRecipeId());
		savedRecipe.addIngredient(new Ingredient());
		savedRecipe.getIngredients().iterator().next().setId("3");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(new Recipe()));
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));
		
		// when
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand).block();

		// then
		assertEquals("3", savedCommand.getId());
		verify(recipeReactiveRepository, times(1)).findById(anyString());
		verify(recipeReactiveRepository, times(1)).save(any(Recipe.class));

	}

	@Test
	public void testSaveRecipeCommandWithIngredientFound() throws Exception {
		// given
		UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		unitOfMeasure.setId("1");
				
		Recipe recipe  = new Recipe();
		recipe.setId("1");
		recipe.addIngredient(new Ingredient());
		recipe.getIngredients().iterator().next().setId("3");
		recipe.getIngredients().iterator().next().setUom(unitOfMeasure);
		
		UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
		unitOfMeasureCommand.setId("1");
		
		IngredientCommand command = new IngredientCommand();
		command.setId("3");
		command.setRecipeId(recipe.getId());
		command.setUom(unitOfMeasureCommand);

		Recipe savedRecipe = new Recipe();
		savedRecipe.setId("1");
		savedRecipe.addIngredient(new Ingredient());
		savedRecipe.getIngredients().iterator().next().setId("3");
		savedRecipe.getIngredients().iterator().next().setUom(unitOfMeasure);

		
		when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
		when(unitOfMeasureRepository.findById(anyString())).thenReturn(Mono.just(unitOfMeasure));
		when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));

		// when
		ingredientService.saveIngredientCommand(command);

		// then
		assertEquals("3", command.getId());
		verify(recipeReactiveRepository, times(1)).findById(anyString());
		verify(recipeReactiveRepository, times(1)).save(any(Recipe.class));

	}
	
	@Test
	public void testDeleteById() throws Exception {
		// given
		Recipe recipe = new Recipe();
		recipe.setId("1");
		Ingredient ingredient = new Ingredient();
		ingredient.setId("3");
		recipe.addIngredient(ingredient);
		ingredient.setRecipe(recipe);

		
		when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
		when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(recipe));
		
		//when
		ingredientService.deleteById("1", "3");

		// then
		verify(recipeReactiveRepository, times(1)).findById(anyString());
		verify(recipeReactiveRepository, times(1)).save(any(Recipe.class));
	}
}