package com.jamargle.bakineando.di;

import android.content.Context;
import com.jamargle.bakineando.BakineandoApp;
import com.jamargle.bakineando.data.JobExecutor;
import com.jamargle.bakineando.data.UiThread;
import com.jamargle.bakineando.domain.interactor.FetchRecipesUseCase;
import com.jamargle.bakineando.domain.interactor.PostExecutionThread;
import com.jamargle.bakineando.domain.interactor.ThreadExecutor;
import com.jamargle.bakineando.domain.interactor.UseCase;
import com.jamargle.bakineando.domain.model.Recipe;
import dagger.Module;
import dagger.Provides;
import java.util.List;
import javax.inject.Singleton;

@Module(
        includes = {
                ViewModelModule.class
        },
        subcomponents = {
                RecipeListFragmentComponent.class,
                RecipeDetailFragmentComponent.class,
                StepDetailFragmentComponent.class
        })
public final class BakingApplicationModule {

    @Provides
    @Singleton
    public ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    @Singleton
    public PostExecutionThread providePostExecutionThread() {
        return new UiThread();
    }

    @Provides
    @Singleton
    Context provideApplicationContext(final BakineandoApp application) {
        return application;
    }

    @Provides
    @Singleton
    public UseCase<Void, List<Recipe>> providesFetchRecipesUseCase(final FetchRecipesUseCase useCase) {
        return useCase;
    }

}
