package com.jamargle.bakineando.presentation.stepdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.presentation.BaseActivity;
import com.jamargle.bakineando.presentation.recipelist.RecipeListActivity;

public final class StepDetailActivity extends BaseActivity
        implements StepDetailFragment.Callback {

    private static final String STEP_TO_SHOW = "param:step_to_show";

    public static Bundle newBundle(final Step stepToShow) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(STEP_TO_SHOW, stepToShow);
        return bundle;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        initToolbar();

        if (savedInstanceState == null) {
            final Step step = getIntent().getParcelableExtra(STEP_TO_SHOW);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_fragment, StepDetailFragment.newInstance(step))
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
            final Step step = getIntent().getParcelableExtra(STEP_TO_SHOW);
            actionBar.setTitle(getString(R.string.step_number, step.getStepNumber()));
        }
    }

}
