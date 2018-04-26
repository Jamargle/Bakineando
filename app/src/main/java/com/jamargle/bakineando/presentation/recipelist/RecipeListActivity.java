package com.jamargle.bakineando.presentation.recipelist;

import android.content.Intent;
import android.os.Bundle;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.presentation.BaseActivity;
import com.jamargle.bakineando.presentation.recipedetail.RecipeDetailActivity;

public final class RecipeListActivity extends BaseActivity
        implements RecipeListFragment.Callback {

    private boolean isTwoPane;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true;
        }
    }

    @Override
    public void onRecipeClicked(final Recipe recipe) {
        startActivity(new Intent(this, RecipeDetailActivity.class));
    }

}
