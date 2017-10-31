package br.com.andrei.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.andrei.domain.Ingredient;

public interface IngredientReactiveRepository extends ReactiveMongoRepository<Ingredient, String>{

}
