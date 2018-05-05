package com.jamargle.bakineando.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.interactor.DefaultObserver;
import com.jamargle.bakineando.domain.interactor.FetchRecipesUseCase;
import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.presentation.widgetconfiguration.BakingWidgetConfigureActivity;
import com.jamargle.bakineando.util.IngredientListUtil;

import java.util.List;

import javax.inject.Inject;

public final class BakingWidget extends AppWidgetProvider {

    @Inject
    FetchRecipesUseCase useCase;

    public static void updateAppWidget(
            @NonNull final Context context,
            @NonNull final AppWidgetManager appWidgetManager,
            final int appWidgetId,
            final String recipeName,
            final List<Ingredient> ingredientList) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.removeAllViews(R.id.widget_ingredients_container);
        for (final Ingredient ingredient : ingredientList) {
            final RemoteViews ingredientView = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_item_list_ingredient);

            final String ingredientLabel = IngredientListUtil.getIngredientLabel(
                    context,
                    ingredient.getName(),
                    ingredient.getQuantity(),
                    ingredient.getMeasure());

            ingredientView.setTextViewText(R.id.ingredient_name, ingredientLabel);
            views.addView(R.id.widget_ingredients_container, ingredientView);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(
            final Context context,
            final AppWidgetManager appWidgetManager,
            final int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (final int appWidgetId : appWidgetIds) {
            useCase.execute(null, new DefaultObserver<List<Recipe>>() {

                @Override
                public void processOnNext(final List<Recipe> recipes) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, recipes.get(0).getName(), recipes.get(0).getIngredients());
                }

            });
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BakingWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

}

