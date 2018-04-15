package com.jamargle.bakineando.presentation.recipelist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.di.ViewModelFactory;
import com.jamargle.bakineando.presentation.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;

public final class RecipeListFragment extends BaseFragment<RecipeListFragment.Callback> {

    @BindView(R.id.recipes_recycler_view) RecyclerView recipeListView;

    @Inject ViewModelFactory viewModelFactory;

    private RecipeListViewModel recipeListViewModel;

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
    }

    private void initViewModel() {
        recipeListViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeListViewModel.class);
    }

    interface Callback {

    }

}
