package br.com.andrei.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.andrei.bootstrap.RecipeBootstrap;
import br.com.andrei.domain.UnitOfMeasure;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;
    
    @Autowired
    RecipeRepository recipeRepository;
    
    @Autowired
    CategoryRepository categoryRepository;
    
    @Before
    public void setUp() throws Exception{
    	 RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository, unitOfMeasureRepository);
    	 recipeBootstrap.onApplicationEvent(null);
    }
    
    @After
    public void deleteAll() throws Exception{
    	unitOfMeasureRepository.deleteAll();
    	recipeRepository.deleteAll();
    	categoryRepository.deleteAll();
    }

    @Test
    public void findByDescription() throws Exception {

        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");

        assertEquals("Teaspoon", uomOptional.get().getDescription());
    }

    @Test
    public void findByDescriptionCup() throws Exception {

        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Cup");

        assertEquals("Cup", uomOptional.get().getDescription());
    }

}