package com.jamargle.bakineando.presentation.recipelist;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.presentation.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class RecipeListFragment extends BaseFragment<RecipeListFragment.Callback> {

    @BindView(R.id.recipe_list) RecyclerView recipeListView;

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        return rootView;
    }

    @Override
    protected boolean isToBeRetained() {
        return true;
    }

    private void initRecyclerView() {

    interface Callback {

    }

}
