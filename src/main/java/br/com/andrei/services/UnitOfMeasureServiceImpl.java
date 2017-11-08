package br.com.andrei.services;

import org.springframework.stereotype.Service;

import br.com.andrei.commands.UnitOfMeasureCommand;
import br.com.andrei.converters.UnitOfMeasureToUnitOfMeasureCommand;
import br.com.andrei.repositories.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

	private UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
	private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

	public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
			UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
		this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
	}

	@Override
	public Flux<UnitOfMeasureCommand> listAllUoms() {

		return unitOfMeasureReactiveRepository.findAll().map(unitOfMeasureToUnitOfMeasureCommand::convert);
	}

}
