package com.jamargle.bakineando.presentation.recipedetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.di.ViewModelFactory;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.presentation.BaseFragment;
import com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeDetailsAdapter;
import com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeDetailsAdapterItemsUtil;
import com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public final class RecipeDetailFragment extends BaseFragment<RecipeDetailFragment.Callback>
        implements RecipeDetailsAdapter.OnStepClickListener {

    private static final String RECIPE_TO_SHOW = "key:RecipeDetailFragment_recipe_to_show";

    @BindView(R.id.recipe_stuff_list) RecyclerView recipeStuffList;

    @Inject ViewModelFactory viewModelFactory;

    private RecipeDetailsAdapter adapter;
    private RecipeDetailViewModel recipeDetailsViewModel;

    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(@NonNull final Recipe recipe) {
        final Bundle args = new Bundle();
        args.putParcelable(RECIPE_TO_SHOW, recipe);
        final RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        return R.layout.fragment_recipe_details;
    }

    @Override
    public void onStepClicked(final Step step) {
        callback.onStepClicked(step);
    }

    private void initRecyclerView() {
        adapter = new RecipeDetailsAdapter(new ArrayList<RecipeItem>(), this);
        recipeStuffList.setAdapter(adapter);
    }

    private void initViewModel() {
        recipeDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeDetailViewModel.class);
        recipeDetailsViewModel.getRecipe().observe(this, new Observer<Recipe>() {

            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    adapter.updateRecipeItemList(RecipeDetailsAdapterItemsUtil.normalizeItems(recipe));
                }
            }

        });

        if (getArguments() != null) {
            recipeDetailsViewModel.setRecipe((Recipe) getArguments().getParcelable(RECIPE_TO_SHOW));
        }
    }

    public interface Callback {

        void onStepClicked(Step step);

    }

}
