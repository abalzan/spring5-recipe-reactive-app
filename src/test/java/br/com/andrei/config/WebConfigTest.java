package br.com.andrei.config;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import br.com.andrei.domain.Recipe;
import br.com.andrei.services.RecipeService;
import reactor.core.publisher.Flux;

@Ignore
public class WebConfigTest {

	WebTestClient webTestClient;

	@Mock
	RecipeService recipeService;

	@Before
	private void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		WebConfig webConfig = new WebConfig();

		RouterFunction<?> routerFunction = webConfig.routes(recipeService);

		webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
	}

	@Test
	public void getRecipesTest() {

		when(recipeService.getRecipes()).thenReturn(Flux.just());

		webTestClient.get().uri("/api/recipes")
						   .accept(MediaType.APPLICATION_JSON)
						   .exchange()
						   .expectStatus()
						   .isOk();

	}

	@Test
	public void getRecipesWithDataTest() {

		when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

		webTestClient.get().uri("/api/recipes")
						   .accept(MediaType.APPLICATION_JSON)
						   .exchange()
						   .expectStatus().isOk()
						   .expectBodyList(Recipe.class);

	}
}
