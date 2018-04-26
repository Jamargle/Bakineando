package com.jamargle.bakineando.domain.repository;

import com.jamargle.bakineando.domain.model.Recipe;
import io.reactivex.Single;
import java.util.List;

public interface NetworkRecipeGateway {

    Single<List<Recipe>> obtainRecipes();

}
