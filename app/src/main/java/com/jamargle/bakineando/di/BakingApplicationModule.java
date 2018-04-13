package com.jamargle.bakineando.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
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

}
