package com.jamargle.bakineando.di;

import dagger.Subcomponent;

@Subcomponent(modules = {StepDetailFragmentModule.class})
interface StepDetailFragmentComponent {

    @Subcomponent.Builder
    interface Builder {
        public StepDetailFragmentComponent build();
    }

}
