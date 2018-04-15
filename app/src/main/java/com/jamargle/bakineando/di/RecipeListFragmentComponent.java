package com.jamargle.bakineando.di;

import dagger.Subcomponent;

@Subcomponent(modules = {RecipeListFragmentModule.class})
interface RecipeListFragmentComponent {

    @Subcomponent.Builder
    interface Builder {
        public RecipeListFragmentComponent build();
    }

}
