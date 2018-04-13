package com.jamargle.bakineando.di;

import android.arch.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
