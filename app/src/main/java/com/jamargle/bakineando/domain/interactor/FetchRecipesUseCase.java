package com.jamargle.bakineando.domain.interactor;

import android.support.annotation.Nullable;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.repository.RecipesRepository;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;

public final class FetchRecipesUseCase extends UseCase<Void, List<Recipe>> {

    private final RecipesRepository recipesRepository;

    @Inject
    public FetchRecipesUseCase(
            final RecipesRepository recipesRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread) {

        super(threadExecutor, postExecutionThread);
        this.recipesRepository = recipesRepository;
    }

    @Override
    protected Observable<List<Recipe>> buildUseCaseObservable(@Nullable final Void params) {
        return recipesRepository.getRecipes().toObservable();
    }

}
