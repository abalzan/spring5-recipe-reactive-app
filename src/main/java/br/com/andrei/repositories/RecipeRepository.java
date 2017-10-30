package br.com.andrei.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.andrei.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
