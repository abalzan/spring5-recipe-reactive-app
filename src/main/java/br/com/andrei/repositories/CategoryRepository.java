package br.com.andrei.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.andrei.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, String> {

	Optional<Category> findByDescription(String description);
}
