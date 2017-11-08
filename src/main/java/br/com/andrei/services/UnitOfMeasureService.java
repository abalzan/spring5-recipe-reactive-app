package br.com.andrei.services;

import br.com.andrei.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {

	//A Reactive Streams Publisher with rx operators that emits 0 to N elements, and then completes (successfully or with an error).
	Flux<UnitOfMeasureCommand> listAllUoms();
}
