package com.jamargle.bakineando.di;

import com.jamargle.bakineando.data.JobExecutor;
import com.jamargle.bakineando.data.UiThread;
import com.jamargle.bakineando.domain.interactor.PostExecutionThread;
import com.jamargle.bakineando.domain.interactor.ThreadExecutor;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module(
        includes = {
                ViewModelModule.class
        },
        subcomponents = {
                RecipeListFragmentComponent.class,
                RecipeDetailFragmentComponent.class
        })
public final class BakingApplicationModule {

    public static final String SCHEDULER_MAIN_THREAD = "mainThread_scheduler";
    public static final String SCHEDULER_IO = "io_scheduler";

    @Provides
    @Singleton
    @Named(SCHEDULER_MAIN_THREAD)
    public Scheduler provideMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @Named(SCHEDULER_IO)
    public Scheduler provideIoScheduler() {
        return Schedulers.io();
    }

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

}
