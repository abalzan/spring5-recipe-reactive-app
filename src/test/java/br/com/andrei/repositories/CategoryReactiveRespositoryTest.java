package br.com.andrei.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.andrei.domain.Category;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CategoryReactiveRespositoryTest {

	private static final String CATEGORY_DESCRIPTION = "Category Description Test";
	
	@Autowired
	CategoryReactiveRepository categoryReactiveRepository;
	
	@Before
	public void setUp() throws Exception {
		categoryReactiveRepository.deleteAll().block();
	}

	@Test
	public void categorySaveTest() {
		Category category = new Category();
		category.setDescription(CATEGORY_DESCRIPTION);
		
		categoryReactiveRepository.save(category).block();
		
		assertEquals(Long.valueOf(1L), categoryReactiveRepository.count().block());
	}
	
	@Test
	public void findByDescriptionTest() {

		
		
		Category category = new Category();
		
		category.setDescription(CATEGORY_DESCRIPTION);
		
		categoryReactiveRepository.save(category).then().block();
		
		Category findResult = categoryReactiveRepository.findByDescription(CATEGORY_DESCRIPTION).block();
		
		assertEquals(CATEGORY_DESCRIPTION, findResult.getDescription());
		assertNotNull(findResult.getId());
	}

}
