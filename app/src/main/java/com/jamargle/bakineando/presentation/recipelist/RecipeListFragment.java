package com.jamargle.bakineando.presentation.recipelist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.di.ViewModelFactory;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.presentation.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public final class RecipeListFragment extends BaseFragment<RecipeListFragment.Callback>
        implements RecipeListAdapter.OnRecipeClickListener {

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
    protected boolean isToBeRetained() {
        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_recipe_list;
    }

    private void initRecyclerView() {
        // TODO Remove dummy recipes when real recipes are ready
        final ArrayList<Recipe> dummyRecipes = new ArrayList<>();
        dummyRecipes.add(new Recipe.Builder().id(1).name("recipe 1").build());
        dummyRecipes.add(new Recipe.Builder().id(2).name("recipe 2").build());

        recipeListView.setHasFixedSize(true);
        adapter = new RecipeListAdapter(new ArrayList<Recipe>(), this);
        recipeListView.setAdapter(adapter);
    }

    private void initViewModel() {
        recipeListViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeListViewModel.class);
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> recipes) {
                Log.d("asdasdas", "recipes Changed:" + recipes);
                adapter.updateRecipes(recipes);
            }
        });
    }

    @Override
    public void onRecipeClicked(final Recipe recipe) {
        //TODO Implement recipe list click callback
        Toast.makeText(getContext(), "Recipe " + recipe.getName() + " clicked", Toast.LENGTH_SHORT).show();
        callback.onRecipeClicked(recipe);
    }

    interface Callback {

        void onRecipeClicked(Recipe recipe);

    }

}
