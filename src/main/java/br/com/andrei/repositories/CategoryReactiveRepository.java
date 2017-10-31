package br.com.andrei.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.andrei.domain.Category;
import reactor.core.publisher.Mono;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {

	//we need one result back, because of this Mono is used
	//The rx operators will offer aliases for input Mono type to preserve the "at most one" property of the resulting Mono.
	Mono<Category> findByDescription(String description);
}
