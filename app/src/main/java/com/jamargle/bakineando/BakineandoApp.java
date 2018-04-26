package com.jamargle.bakineando;

import android.app.Activity;
import android.app.Application;
import com.facebook.stetho.Stetho;
import com.jamargle.bakineando.di.DaggerBakingApplicationComponent;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;

public final class BakineandoApp extends Application implements HasActivityInjector {

    @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerBakingApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

        Stetho.initializeWithDefaults(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

}
