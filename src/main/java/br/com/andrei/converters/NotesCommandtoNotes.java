package br.com.andrei.converters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotesCommandtoNotes {

	private Long id;
	private RecipeCommandToRecipe recipe;
	private String recipeNotes;

}
