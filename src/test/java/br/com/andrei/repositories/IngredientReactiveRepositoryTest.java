package br.com.andrei.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.andrei.domain.Ingredient;

@RunWith(SpringRunner.class)
@DataMongoTest
public class IngredientReactiveRepositoryTest {

	@Autowired
	IngredientReactiveRepository ingredientReactiveRepository;
	
	@Before
	public void setUp() throws Exception {
		ingredientReactiveRepository.deleteAll().block();
	}

	@Test
	public void saveIngredientTest() {
		Ingredient ingredient = new Ingredient();
		ingredient.setDescription("Ingredient Description Test");
		
		ingredientReactiveRepository.save(ingredient).block();
		
		assertEquals(Long.valueOf(1L), ingredientReactiveRepository.count().block());
	}
	
}
