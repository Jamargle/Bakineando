package com.jamargle.bakineando.presentation.recipelist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.di.ViewModelFactory;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.presentation.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public final class RecipeListFragment extends BaseFragment<RecipeListFragment.Callback>
        implements RecipeListAdapter.OnRecipeClickListener,
        RecipeListAdapter.OnRecipeCountListener {

    private static final String SAVED_SCROLL_POSITION = "key:RecipeListFragment_scroll_position";
    private static final String SAVED_SELECTED_POSITION = "key:RecipeListFragment_selected_position";

    @BindView(R.id.recipes_recycler_view) RecyclerView recipeListView;
    @BindView(R.id.empty_list) TextView emptyListView;
    @BindView(R.id.recipes_loading) ProgressBar loadingView;

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
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_SCROLL_POSITION)
                    && !savedInstanceState.containsKey(SAVED_SELECTED_POSITION)) {

                final int position = savedInstanceState.getInt(SAVED_SCROLL_POSITION);
                scrollToSavedScrollPosition(position);
            }
            if (savedInstanceState.containsKey(SAVED_SELECTED_POSITION)) {
                final int position = savedInstanceState.getInt(SAVED_SELECTED_POSITION);
                adapter.setSelectedPosition(position);
                scrollToSavedScrollPosition(position);
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        final int position = ((LinearLayoutManager) recipeListView.getLayoutManager()).findLastVisibleItemPosition();
        outState.putInt(SAVED_SCROLL_POSITION, position);
        if (adapter.getSelectedPosition() >= 0) {
            outState.putInt(SAVED_SELECTED_POSITION, adapter.getSelectedPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        startSeeingRecipes();
    }

    @Override
    protected boolean isToBeRetained() {
        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_recipe_list;
    }

    @Override
    public void onRecipeClicked(final Recipe recipe) {
        callback.onRecipeClicked(recipe);
    }

    @Override
    public void onNoRecipesToShow() {
        recipeListView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRecipesToShowPrepared() {
        recipeListView.setVisibility(View.VISIBLE);
        emptyListView.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        recipeListView.setHasFixedSize(true);
        adapter = new RecipeListAdapter(new ArrayList<Recipe>(), this, this);
        recipeListView.setAdapter(adapter);
    }

    private void initViewModel() {
        recipeListViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeListViewModel.class);
        callback.onRecipesRetrieving();
        recipeListViewModel.getLoadingState().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(@Nullable final Boolean isLoading) {
                if (isLoading == null) {
                    return;
                }
                if (isLoading) {
                    showLoading();
                } else {
                    hideLoading();
                }
            }

        });
    }

    private void showLoading() {
        if (loadingView != null) {
            recipeListView.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
        }
    }


    private void hideLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
            recipeListView.setVisibility(View.VISIBLE);
        }
    }

    private void scrollToSavedScrollPosition(final int position) {
        final Handler handler = new Handler();
        final int delay = 200;  // Add some delay to perform the scroll after the view is initialized
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (RecyclerView.NO_POSITION != position) {
                    final LinearLayoutManager layoutManager =
                            (LinearLayoutManager) recipeListView.getLayoutManager();
                    layoutManager.smoothScrollToPosition(recipeListView, null, position);
                }
            }
        }, delay);
    }

    private boolean hasNetworkConnection() {
        if (getActivity() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

    private void startSeeingRecipes() {
        if (hasNetworkConnection()) {
            recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {

                @Override
                public void onChanged(@Nullable final List<Recipe> recipes) {
                    adapter.updateRecipes(recipes);
                    callback.onRecipesReady();
                }

            });
        } else {
            showNoInternetConnectionMessage();
        }
    }

    private void showNoInternetConnectionMessage() {
        if (getActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.no_internet_title)
                .setMessage(R.string.enable_internet_message)
                .setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                    public void onClick(
                            final DialogInterface dialog,
                            final int which) {

                        openDeviceSettings();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(
                            final DialogInterface dialog,
                            final int which) {

                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void openDeviceSettings() {
        callback.openDeviceSettings();
    }

    interface Callback {

        void onRecipesRetrieving();

        void onRecipesReady();

        void onRecipeClicked(Recipe recipe);

        void openDeviceSettings();

    }

}
