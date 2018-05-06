package com.jamargle.bakineando.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.presentation.widget.WidgetViewsFactory;

public final class BakingWidget extends AppWidgetProvider {

    public static void updateAppWidget(
            @NonNull final Context context,
            @NonNull final AppWidgetManager appWidgetManager,
            final int appWidgetId,
            final int recipeId,
            final String recipeName) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.setRemoteAdapter(
                R.id.widget_ingredients_list,   // Id of the layout holding the ListView
                // The intent to start the service to fetch the data shown in the widget
                WidgetViewsFactory.getWidgetViewsFactoryIntent(context, recipeId));

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
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}

