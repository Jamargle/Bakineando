package com.jamargle.bakineando.di;

import android.app.Application;
import com.jamargle.bakineando.BakineandoApp;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
        BakingApplicationModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBuilder.class,
        RepositoryModule.class,
        RoomDatabaseModule.class
})
public interface BakingApplicationComponent extends AndroidInjector<BakineandoApp> {

    void inject(Application application);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(BakineandoApp application);

        BakingApplicationComponent build();

    }

}
