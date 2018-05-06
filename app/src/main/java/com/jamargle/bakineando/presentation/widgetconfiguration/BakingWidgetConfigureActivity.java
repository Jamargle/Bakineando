package com.jamargle.bakineando.presentation.widgetconfiguration;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.presentation.BaseActivity;
import com.jamargle.bakineando.util.SharedPreferencesHandler;
import com.jamargle.bakineando.widget.BakingWidget;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class BakingWidgetConfigureActivity extends BaseActivity {

    @BindView(R.id.ingredients_radio_group) RadioGroup ingredientsRadioGroup;

    @Inject SharedPreferencesHandler preferencesHandler;

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.baking_widget_configure);
        ButterKnife.bind(this);

        appWidgetId = findWidgetIdFromIntent();

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        initWidgetView();
    }

    @OnClick(R.id.ok_button)
    public void onClickOkButton() {
        final int checkedItemId = ingredientsRadioGroup.getCheckedRadioButtonId();
        final String recipeName = ((AppCompatRadioButton) ingredientsRadioGroup.getChildAt(checkedItemId)).getText().toString();
        preferencesHandler.setWidgetRecipeTitle(recipeName);

        final Context context = getApplicationContext();
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        final ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient.Builder().ingredient("Ingredasdas 1").build());
        ingredients.add(new Ingredient.Builder().ingredient("Ingredasdas 2").build());
        BakingWidget.updateAppWidget(context, appWidgetManager, appWidgetId, recipeName,
                /*usecase.getIngredients*/ingredients);

        final Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private int findWidgetIdFromIntent() {
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        return AppWidgetManager.INVALID_APPWIDGET_ID;
    }

    private void initWidgetView() {
        final Set<String> recipeNames = preferencesHandler.getRecipeNames();

        if (recipeNames.size() == 0) {
            Toast.makeText(this, R.string.widget_no_recipe_names_warning, Toast.LENGTH_SHORT).show();
            finish();
        }

        int currentIndex = 0;

        for (final String name : recipeNames) {
            final AppCompatRadioButton radioButton = new AppCompatRadioButton(this);
            radioButton.setText(name);
            radioButton.setId(currentIndex++);
            ingredientsRadioGroup.addView(radioButton);
        }

        if (ingredientsRadioGroup.getChildCount() > 0) {
            ((AppCompatRadioButton) ingredientsRadioGroup.getChildAt(0)).setChecked(true);
        }
    }

}

