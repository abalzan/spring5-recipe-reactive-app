package br.com.andrei.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.andrei.domain.UnitOfMeasure;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {

	//The rx operators will offer aliases for input Mono type to preserve the "at most one" property of the resulting Mono.
	Mono<UnitOfMeasure> findByDescription(String description);

}
