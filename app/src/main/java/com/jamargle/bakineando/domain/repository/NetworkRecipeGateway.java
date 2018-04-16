package com.jamargle.bakineando.domain.repository;

import android.arch.lifecycle.LiveData;
import com.jamargle.bakineando.domain.model.Recipe;
import java.util.List;

public interface NetworkRecipeGateway {

    LiveData<List<Recipe>> obtainRecipes();

}
