package br.com.andrei.services;

import java.util.Set;

import br.com.andrei.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {

	Set<UnitOfMeasureCommand> listAllUoms();
}
