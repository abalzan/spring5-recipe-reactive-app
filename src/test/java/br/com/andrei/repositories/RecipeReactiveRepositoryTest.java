package br.com.andrei.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.andrei.domain.Recipe;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveRepositoryTest {

	@Autowired
	RecipeReactiveRepository recipeReactiveRepository;
	
	@Before
	public void setUp() throws Exception {
		recipeReactiveRepository.deleteAll().block();
	}

	@Test
	public void saveRecipeTest() {
		Recipe recipe = new Recipe();
		recipe.setDescription("Recipe Description Test");
		
		recipeReactiveRepository.save(recipe).block();
		
		assertEquals(Long.valueOf(1L), recipeReactiveRepository.count().block());
	}

}
