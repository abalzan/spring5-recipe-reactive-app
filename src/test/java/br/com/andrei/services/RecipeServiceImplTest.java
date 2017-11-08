package br.com.andrei.services;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.andrei.commands.RecipeCommand;
import br.com.andrei.converters.RecipeCommandToRecipe;
import br.com.andrei.converters.RecipeToRecipeCommand;
import br.com.andrei.domain.Recipe;
import br.com.andrei.exceptions.NotFoundException;
import br.com.andrei.repositories.RecipeReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeServiceImpl(recipeReactiveRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    public void getRecipeByIdTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipeService.findById("1").block();

        assertNotNull("Null recipe returned", recipeReturned);
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    public void getRecipeByIdTestNotFound() throws Exception {

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.empty());

        Recipe recipeReturned = recipeService.findById("1").block();

        assertNull(recipeReturned);
    }

    @Test
    public void getRecipeCommandByIdTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeService.findCommandById("1").block();

        assertNotNull("Null recipe returned", commandById);
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    public void getRecipesTest() throws Exception {

        Recipe recipe1 = new Recipe();
        recipe1.setId("1");
        
        Recipe recipe2 = new Recipe();
        recipe1.setId("2");

        when(recipeService.getRecipes()).thenReturn(Flux.just(recipe1, recipe2));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();

        assertEquals(recipes.size(), 2);
        verify(recipeReactiveRepository, times(1)).findAll();
        verify(recipeReactiveRepository, never()).findById(anyString());
    }

    @Test
    public void testDeleteById() throws Exception {

        //given
        String idToDelete = "2";

        when(recipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());
        
        //when
        recipeService.deleteById(idToDelete).block();

        //then
        verify(recipeReactiveRepository, times(1)).deleteById(anyString());
    }
}