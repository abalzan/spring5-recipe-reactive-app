package br.com.andrei.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.andrei.domain.Recipe;
import br.com.andrei.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

	private final RecipeRepository recipeRepository;

	public ImageServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Override
	public void saveImageFile(String recipeId, MultipartFile file) {

		try {
			Recipe recipe = recipeRepository.findById(recipeId).get();
			Byte[] byteObjects = new Byte[file.getBytes().length];

			int i = 0;

			for (Byte b : file.getBytes()) {
				byteObjects[i++] = b;
			}

			recipe.setImage(byteObjects);

			recipeRepository.save(recipe);
		} catch (IOException e) {
			log.error("An error occured: " + e);
			e.printStackTrace();
		}

	}

}
