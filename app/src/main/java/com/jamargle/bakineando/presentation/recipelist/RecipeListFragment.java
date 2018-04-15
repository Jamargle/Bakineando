package com.jamargle.bakineando.presentation.recipelist;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.presentation.BaseFragment;

import butterknife.BindView;

public final class RecipeListFragment extends BaseFragment<RecipeListFragment.Callback> {

    @BindView(R.id.recipe_list) RecyclerView recipeListView;

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);
        initRecyclerView();
        initViewModel();
        return view;
    }

    @Override
    protected boolean isToBeRetained() {
        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_recipe_list;
    }

    private void initRecyclerView() {

    interface Callback {

    }

}
