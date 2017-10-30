package br.com.andrei.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import br.com.andrei.commands.CategoryCommand;
import br.com.andrei.domain.Category;
import lombok.Synchronized;

/**
 * Created by jt on 6/21/17.
 */
@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {

	@Synchronized
	@Nullable
	@Override
	public CategoryCommand convert(Category source) {
		if (source == null) {
			return null;
		}

		final CategoryCommand categoryCommand = new CategoryCommand();

		categoryCommand.setId(source.getId());
		categoryCommand.setDescription(source.getDescription());

		return categoryCommand;
	}
}
