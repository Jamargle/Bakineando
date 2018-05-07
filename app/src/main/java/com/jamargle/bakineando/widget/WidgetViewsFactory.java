package com.jamargle.bakineando.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.data.local.BakingDb;
import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.util.IngredientListUtil;
import java.util.ArrayList;
import java.util.List;

public final class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String RECIPE_ID = "Key:WidgetViewsFactory_recipe_id";

    private Context context;
    private List<Ingredient> dataSet;
    private int recipeId;

    public WidgetViewsFactory(
            final Context context,
            final Intent intent) {

        this.context = context;
        recipeId = intent.getIntExtra(RECIPE_ID, 0);
    }

    public static Intent getWidgetViewsFactoryIntent(
            final Context context,
            final int recipeId) {

        final Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(RECIPE_ID, recipeId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        return intent;
    }

    @Override
    public void onCreate() {
        dataSet = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        dataSet.clear();

        final List<Ingredient> ingredientsForRecipe = BakingDb.getInstance(context)
                .getIngredientDao()
                .getIngredientsForRecipe(recipeId);

        dataSet.addAll(ingredientsForRecipe);
    }

    @Override
    public void onDestroy() {
        dataSet.clear();
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public RemoteViews getViewAt(final int position) {
        final Ingredient ingredient = dataSet.get(position);
        final String ingredientLabel = IngredientListUtil.getIngredientLabel(
                context,
                ingredient.getName(),
                ingredient.getQuantity(),
                ingredient.getMeasure());

        final RemoteViews ingredientWidgetItem = new RemoteViews(
                context.getPackageName(),
                R.layout.widget_item_list_ingredient);
        ingredientWidgetItem.setTextViewText(R.id.ingredient_widget_label, ingredientLabel);

        return ingredientWidgetItem;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
