package br.com.andrei.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.andrei.domain.Recipe;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
