package br.com.andrei.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.andrei.domain.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String>{

}
