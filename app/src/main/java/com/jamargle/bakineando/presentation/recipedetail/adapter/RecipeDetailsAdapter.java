package com.jamargle.bakineando.presentation.recipedetail.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.domain.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem.INGREDIENT;
import static com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem.STEP;

public final class RecipeDetailsAdapter
        extends RecyclerView.Adapter<RecipeDetailsAdapter.ViewHolder> {

    private final List<RecipeItem> dataSet;
    private final OnStepClickListener listener;

    public RecipeDetailsAdapter(
            @NonNull final List<RecipeItem> dataSet,
            @NonNull final OnStepClickListener listener) {

        this.dataSet = dataSet;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent,
            final int viewType) {

        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case INGREDIENT:
                return new IngredientViewHolder(layoutInflater
                        .inflate(R.layout.item_list_ingredient, parent, false));
            case STEP:
            default:
                return new StepViewHolder(layoutInflater
                        .inflate(R.layout.item_list_step, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull final ViewHolder viewHolder,
            final int position) {

        switch (getItemViewType(position)) {
            case INGREDIENT:
                ((IngredientViewHolder) viewHolder).bindItem((IngredientItem) dataSet.get(position));
                break;
            case STEP:
            default:
                ((StepViewHolder) viewHolder).bindItem((StepItem) dataSet.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return dataSet.get(position).getViewType();
    }

    public void updateRecipeItemList(final List<RecipeItem> newDataSet) {
        dataSet.clear();
        dataSet.addAll(newDataSet);

        notifyDataSetChanged();
    }

    public interface OnStepClickListener {

        void onStepClicked(Step step);

    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        private ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    final class IngredientViewHolder extends ViewHolder {

        @BindView(R.id.ingredient_name) TextView ingredientName;
        @BindView(R.id.ingredient_quantity) TextView ingredientQuantity;

        IngredientViewHolder(final View itemView) {
            super(itemView);
        }

        void bindItem(@NonNull final IngredientItem item) {
            final Ingredient ingredient = item.getItem();
            ingredientName.setText(ingredient.getName());
            final String quantityLabel = ingredient.getQuantity() + " " + ingredient.getMeasure();
            ingredientQuantity.setText(quantityLabel);
        }
    }

    final class StepViewHolder extends ViewHolder {

        @BindView(R.id.short_description) TextView shortDescription;

        private Step step;

        StepViewHolder(final View itemView) {
            super(itemView);
        }

        void bindItem(@NonNull final StepItem item) {
            this.step = item.getItem();
            final String label = step.getStepNumber() + " " + step.getShortDescription();
            this.shortDescription.setText(label);
        }

        @OnClick(R.id.step_item_container)
        public void stepItemClicked() {
            listener.onStepClicked(step);
        }

    }

}
