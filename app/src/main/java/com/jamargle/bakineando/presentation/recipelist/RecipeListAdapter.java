package com.jamargle.bakineando.presentation.recipelist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeDataset;
    private final OnRecipeClickListener listener;

    RecipeListAdapter(
            @NonNull final List<Recipe> recipeList,
            @NonNull final OnRecipeClickListener listener) {

        recipeDataset = recipeList;
        this.listener = listener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent,
            final int viewType) {

        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(
            @NonNull final RecipeViewHolder viewHolder,
            int position) {

        if (recipeDataset.size() >= position) {
            viewHolder.bindRecipe(recipeDataset.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return recipeDataset.size();
    }

    void updateRecipes(final List<Recipe> newDataSet) {
        recipeDataset.clear();
        recipeDataset.addAll(newDataSet);

        notifyDataSetChanged();
    }

    public interface OnRecipeClickListener {

        void onRecipeClicked(Recipe recipe);

    }

    final class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_name) TextView recipeName;
        @BindView(R.id.recipe_servings) TextView recipeServings;

        RecipeViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.recipe_item_container)
        void onRecipeClicked() {
            listener.onRecipeClicked(recipeDataset.get(getAdapterPosition()));
        }

        void bindRecipe(final Recipe recipe) {
            recipeName.setText(recipe.getName());
            final String servingsLabel = recipeServings.getResources()
                    .getString(R.string.servings_number, recipe.getServings());
            recipeServings.setText(servingsLabel);
        }
    }

}
