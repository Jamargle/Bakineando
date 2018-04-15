package com.jamargle.bakineando.di;

import dagger.Subcomponent;

@Subcomponent(modules = {RecipeDetailFragmentModule.class})
interface RecipeDetailFragmentComponent {

    @Subcomponent.Builder
    interface Builder {
        public RecipeDetailFragmentComponent build();
    }

}
