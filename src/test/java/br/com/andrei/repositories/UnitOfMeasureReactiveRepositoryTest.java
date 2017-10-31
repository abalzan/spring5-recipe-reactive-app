package br.com.andrei.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.andrei.domain.UnitOfMeasure;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureReactiveRepositoryTest {

	private static final String UOM_DESCRIPTION = "Unit of Measure Description";
    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    
    @Before
    public void setUp() throws Exception{
    	unitOfMeasureReactiveRepository.deleteAll().block();
    }
    
    @Test
    public void saveUnitOfMeasureTest() throws Exception {

    	UnitOfMeasure uom = new UnitOfMeasure();
    	uom.setDescription(UOM_DESCRIPTION);
    	
    	unitOfMeasureReactiveRepository.save(uom).block();
    	
    	assertEquals(Long.valueOf(1L), unitOfMeasureReactiveRepository.count().block());
    }

    @Test
    public void findByDescriptionTest() throws Exception {

    	UnitOfMeasure uom = new UnitOfMeasure();
    	uom.setDescription(UOM_DESCRIPTION);
    	
    	unitOfMeasureReactiveRepository.save(uom).then().block();
    	
    	UnitOfMeasure uomResult = unitOfMeasureReactiveRepository.findByDescription(UOM_DESCRIPTION).block(); 

        assertEquals(UOM_DESCRIPTION, uomResult.getDescription());
        assertNotNull(uomResult.getId());
    }

}