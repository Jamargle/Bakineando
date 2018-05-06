package com.jamargle.bakineando.presentation.widgetconfiguration;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.di.ViewModelFactory;
import com.jamargle.bakineando.domain.interactor.DefaultObserver;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.presentation.BaseActivity;
import com.jamargle.bakineando.widget.BakingWidget;
import java.util.List;
import javax.inject.Inject;

public final class BakingWidgetConfigureActivity extends BaseActivity {

    @BindView(R.id.ingredients_radio_group) RadioGroup ingredientsRadioGroup;

    @Inject ViewModelFactory viewModelFactory;

    private BakingWidgetConfigureViewModel viewModel;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.baking_widget_configure);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BakingWidgetConfigureViewModel.class);

        appWidgetId = findWidgetIdFromIntent();

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        initWidgetConfigurationView();
    }

    @OnClick(R.id.ok_button)
    public void onClickOkButton() {
        final int checkedItemId = ingredientsRadioGroup.getCheckedRadioButtonId();
        final String recipeName = getSelectedRecipeName(checkedItemId);

        final Context context = getApplicationContext();
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        BakingWidget.updateAppWidget(context, appWidgetManager, appWidgetId, checkedItemId, recipeName);

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

    private void initWidgetConfigurationView() {
        viewModel.getRecipes(new DefaultObserver<List<Recipe>>() {

            @Override
            public void processOnNext(final List<Recipe> recipes) {
                if (recipes.isEmpty()) {
                    Toast.makeText(BakingWidgetConfigureActivity.this, R.string.widget_no_recipe_names_warning, Toast.LENGTH_SHORT).show();
                    finish();
                }

                for (final Recipe recipe : recipes) {
                    final AppCompatRadioButton radioButton = new AppCompatRadioButton(BakingWidgetConfigureActivity.this);
                    radioButton.setText(recipe.getName());
                    radioButton.setId(recipe.getId());
                    ingredientsRadioGroup.addView(radioButton);
                }

                if (ingredientsRadioGroup.getChildCount() > 0) {
                    ((AppCompatRadioButton) ingredientsRadioGroup.getChildAt(0)).setChecked(true);
                }
            }

        });
    }

    private String getSelectedRecipeName(final int checkedItemId) {
        for (int i = 0; i < ingredientsRadioGroup.getChildCount(); i++) {
            if (checkedItemId == ingredientsRadioGroup.getChildAt(i).getId()) {
                return ((AppCompatRadioButton) ingredientsRadioGroup.getChildAt(i)).getText().toString();
            }
        }
        return null;
    }

}

