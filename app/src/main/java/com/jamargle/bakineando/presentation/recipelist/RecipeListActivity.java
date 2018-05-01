package com.jamargle.bakineando.presentation.recipelist;

import android.content.Intent;
import android.os.Bundle;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.presentation.BaseActivity;
import com.jamargle.bakineando.presentation.recipedetail.RecipeDetailActivity;
import com.jamargle.bakineando.presentation.recipedetail.RecipeDetailFragment;

public final class RecipeListActivity extends BaseActivity
        implements RecipeListFragment.Callback {

    private boolean isTwoPane;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        initFragments();
    }

    @Override
    public void onRecipeClicked(final Recipe recipe) {
        showNewRecipeDetailsFragment(recipe);
    }

    private void initFragments() {
        checkIfTwoPaneVersion();
    }

    private void checkIfTwoPaneVersion() {
        if (findViewById(R.id.recipe_detail_container) != null) {
            isTwoPane = true;
        }
    }

    private void showNewRecipeDetailsFragment(final Recipe recipe) {
        if (isTwoPane) {
            // TODO Set up the recipe detail fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, new RecipeDetailFragment())
                    .commit();
        } else {
            final Intent intent = new Intent(this, RecipeDetailActivity.class);
            startActivity(intent.putExtras(RecipeDetailActivity.newBundle(recipe)));
        }
    }

}
