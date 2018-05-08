package com.jamargle.bakineando.presentation.recipedetail.adapter;

import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.exoplayer2.ui.PlayerView;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.util.IngredientListUtil;
import com.jamargle.bakineando.util.stickyheader.StickyHeaderAdapter;
import java.util.List;

import static com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem.INGREDIENT;
import static com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem.INTRODUCTION;
import static com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem.STEP;

public final class RecipeDetailsAdapter
        extends RecyclerView.Adapter<RecipeDetailsAdapter.BaseViewHolder>
        implements StickyHeaderAdapter<RecipeDetailsAdapter.HeaderViewHolder> {

    private final List<RecipeItem> dataSet;
    private final List<String> headers;
    private final OnStepClickListener listener;
    private final VideoItemListener videoListener;

    public RecipeDetailsAdapter(
            @NonNull final List<RecipeItem> dataSet,
            @NonNull final List<String> headers,
            @NonNull final OnStepClickListener listener,
            @NonNull final VideoItemListener videoListener) {

        this.dataSet = dataSet;
        this.headers = headers;
        this.listener = listener;
        this.videoListener = videoListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent,
            final int viewType) {

        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case INTRODUCTION:
                return new IntroductionViewHolder(
                        layoutInflater.inflate(R.layout.item_list_introduction, parent, false));
            case INGREDIENT:
                return new IngredientViewHolder(
                        layoutInflater.inflate(R.layout.item_list_ingredient, parent, false));
            case STEP:
            default:
                return new StepViewHolder(
                        layoutInflater.inflate(R.layout.item_list_step, parent, false));
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull final BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof IntroductionViewHolder) {
            videoListener.onVideoViewToBeStopped();
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull final BaseViewHolder viewHolder,
            final int position) {

        switch (getItemViewType(position)) {
            case INTRODUCTION:
                ((IntroductionViewHolder) viewHolder).bindItem((IntroductionItem) dataSet.get(position));
                break;
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

    @Override
    public long getHeaderId(final int position) {
        return dataSet.get(position).getHeaderPosition();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new HeaderViewHolder(inflater.inflate(R.layout.item_list_recipe_detail_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(
            final HeaderViewHolder viewHolder,
            final int position) {

        viewHolder.bindHeader(dataSet.get(position).viewType, headers.get((int) getHeaderId(position)));
    }

    public void updateRecipeItemList(final List<RecipeItem> newDataSet) {
        dataSet.clear();
        dataSet.addAll(newDataSet);

        notifyDataSetChanged();
    }

    public interface OnStepClickListener {

        void onStepClicked(Step step);

    }

    public interface VideoItemListener {

        void onVideoViewToBePrepared(
                PlayerView playerView,
                final Uri mediaUri);

        void onVideoViewToBeStopped();

    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_detail_header) TextView headerText;

        HeaderViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindHeader(
                final int viewType,
                final String text) {

            headerText.setText(text);
            @ColorRes final int colorToApply;
            switch (viewType) {
                case INTRODUCTION:
                    colorToApply = android.R.color.white;
                    break;
                case INGREDIENT:
                    colorToApply = R.color.ingredient_header_background;
                    break;
                case STEP:
                default:
                    colorToApply = R.color.step_header_background;
                    break;
            }
            headerText.setBackgroundColor(ContextCompat.getColor(headerText.getContext(), colorToApply));
        }

    }

    final class IntroductionViewHolder extends BaseViewHolder {

        @BindView(R.id.introduction_video) PlayerView videoView;

        IntroductionViewHolder(final View itemView) {
            super(itemView);
        }

        void bindItem(@NonNull final IntroductionItem item) {
            final Step introductionStep = item.getItem();
            if (introductionStep.getVideoURL() != null
                    && !introductionStep.getVideoURL().isEmpty()) {
                videoListener.onVideoViewToBePrepared(videoView, Uri.parse(introductionStep.getVideoURL()));
            }
        }

    }

    final class IngredientViewHolder extends BaseViewHolder {

        @BindView(R.id.ingredient_name) TextView ingredientName;
        @BindView(R.id.ingredient_quantity) TextView ingredientQuantity;

        IngredientViewHolder(final View itemView) {
            super(itemView);
        }

        void bindItem(@NonNull final IngredientItem item) {
            final Ingredient ingredient = item.getItem();
            ingredientName.setText(ingredient.getName());
            final String quantityLabel = IngredientListUtil.getIngredientQuantityLabel(
                    itemView.getContext(),
                    ingredient.getQuantity(),
                    ingredient.getMeasure());
            ingredientQuantity.setText(quantityLabel);
        }
    }

    final class StepViewHolder extends BaseViewHolder {

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
