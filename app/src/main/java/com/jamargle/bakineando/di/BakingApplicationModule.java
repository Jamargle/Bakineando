package com.jamargle.bakineando.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.jamargle.bakineando.BakineandoApp;
import com.jamargle.bakineando.data.JobExecutor;
import com.jamargle.bakineando.data.UiThread;
import com.jamargle.bakineando.domain.interactor.PostExecutionThread;
import com.jamargle.bakineando.domain.interactor.ThreadExecutor;
import com.jamargle.bakineando.util.SharedPreferencesHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                ViewModelModule.class
        },
        subcomponents = {
                RecipeListFragmentComponent.class,
                RecipeDetailFragmentComponent.class
        })
public final class BakingApplicationModule {

    private static final String SHARED_PREFERENCES_NAME = "bakeando_shared_preferences";

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
    public SharedPreferencesHandler provideSharedPreferencesHandler(final Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new SharedPreferencesHandler(sharedPreferences);
    }

}
