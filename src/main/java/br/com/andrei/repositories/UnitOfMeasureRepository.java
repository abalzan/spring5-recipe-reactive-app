package br.com.andrei.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.andrei.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

	Optional<UnitOfMeasure> findByDescription(String description);
}
