package com.jamargle.bakineando.presentation.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.presentation.BaseActivity;
import com.jamargle.bakineando.presentation.recipelist.RecipeListActivity;

import butterknife.ButterKnife;

public final class RecipeDetailActivity extends BaseActivity
        implements RecipeDetailFragment.Callback {

    private static final String RECIPE_TO_SHOW = "param:recipe_to_show";

    public static Bundle newBundle(final Recipe recipeToShow) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_TO_SHOW, recipeToShow);
        return bundle;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
        initToolbar();

        if (savedInstanceState == null) {
            final Recipe recipe = getIntent().getParcelableExtra(RECIPE_TO_SHOW);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_details_fragment, RecipeDetailFragment.newInstance(recipe))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStepClicked(Step step) {
        // TODO Implement step clicked callback
        Toast.makeText(this, "Step clicked " + step.getStepNumber(), Toast.LENGTH_SHORT).show();
    }

}
