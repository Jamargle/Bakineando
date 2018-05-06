package com.jamargle.bakineando.presentation.widgetconfiguration;

import android.arch.lifecycle.ViewModel;
import com.jamargle.bakineando.domain.interactor.DefaultObserver;
import com.jamargle.bakineando.domain.interactor.FetchRecipesUseCase;
import com.jamargle.bakineando.domain.model.Recipe;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class BakingWidgetConfigureViewModel extends ViewModel {

    private final FetchRecipesUseCase useCase;

    @Inject
    public BakingWidgetConfigureViewModel(final FetchRecipesUseCase useCase) {
        this.useCase = useCase;
    }

    void getRecipes(final DefaultObserver<List<Recipe>> observer) {
        useCase.execute(null, observer);
    }

}
