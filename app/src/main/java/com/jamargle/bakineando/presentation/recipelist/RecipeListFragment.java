package com.jamargle.bakineando.presentation.recipelist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.di.ViewModelFactory;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.presentation.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public final class RecipeListFragment extends BaseFragment<RecipeListFragment.Callback>
        implements RecipeListAdapter.OnRecipeClickListener {

    private static final String SAVED_SCROLL_POSITION = "key:RecipeListFragment_scroll_position";
    private static final String SAVED_SELECTED_POSITION = "key:RecipeListFragment_selected_position";

    @BindView(R.id.recipes_recycler_view) RecyclerView recipeListView;

    @Inject ViewModelFactory viewModelFactory;

    private RecipeListAdapter adapter;
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
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_SCROLL_POSITION)
                    && !savedInstanceState.containsKey(SAVED_SELECTED_POSITION)) {

                final int position = savedInstanceState.getInt(SAVED_SCROLL_POSITION);
                scrollToSavedScrollPosition(position);
            }
            if (savedInstanceState.containsKey(SAVED_SELECTED_POSITION)) {
                final int position = savedInstanceState.getInt(SAVED_SELECTED_POSITION);
                adapter.setSelectedPosition(position);
                scrollToSavedScrollPosition(position);
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        final int position = ((LinearLayoutManager) recipeListView.getLayoutManager()).findLastVisibleItemPosition();
        outState.putInt(SAVED_SCROLL_POSITION, position);
        outState.putInt(SAVED_SELECTED_POSITION, adapter.getSelectedPosition());
        super.onSaveInstanceState(outState);
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
        recipeListView.setHasFixedSize(true);
        adapter = new RecipeListAdapter(new ArrayList<Recipe>(), this);
        recipeListView.setAdapter(adapter);
    }

    private void initViewModel() {
        recipeListViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeListViewModel.class);
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {

            @Override
            public void onChanged(@Nullable final List<Recipe> recipes) {
                adapter.updateRecipes(recipes);
            }

        });
    }

    @Override
    public void onRecipeClicked(final Recipe recipe) {
        callback.onRecipeClicked(recipe);
    }

    private void scrollToSavedScrollPosition(final int position) {
        final Handler handler = new Handler();
        final int delay = 200;  // Add some delay to perform the scroll after the view is initialized
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final LinearLayoutManager layoutManager = (LinearLayoutManager) recipeListView.getLayoutManager();
                layoutManager.smoothScrollToPosition(recipeListView, null, position);
            }
        }, delay);
    }

    interface Callback {

        void onRecipeClicked(Recipe recipe);

    }

}
