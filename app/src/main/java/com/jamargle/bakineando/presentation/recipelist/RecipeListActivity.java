package com.jamargle.bakineando.presentation.recipelist;

import android.os.Bundle;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.presentation.BaseActivity;

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

}
