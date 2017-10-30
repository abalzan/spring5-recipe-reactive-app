package br.com.andrei.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document //tables on docker are Document not Entity.
public class UnitOfMeasure {

	@Id
	private String id;
	private String description;
}
