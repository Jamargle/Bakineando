package com.jamargle.bakineando.presentation.recipedetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jamargle.bakineando.BuildConfig;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.di.ViewModelFactory;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.presentation.BaseFragment;
import com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeDetailsAdapter;
import com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeDetailsAdapterItemsUtil;
import com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem;
import com.jamargle.bakineando.util.stickyheader.StickyHeaderDecoration;
import java.util.ArrayList;
import javax.inject.Inject;

public final class RecipeDetailFragment extends BaseFragment<RecipeDetailFragment.Callback>
        implements RecipeDetailsAdapter.OnStepClickListener,
        RecipeDetailsAdapter.VideoItemListener {

    private static final String RECIPE_TO_SHOW = "key:RecipeDetailFragment_recipe_to_show";

    @BindView(R.id.recipe_stuff_list) RecyclerView recipeStuffList;

    @Inject ViewModelFactory viewModelFactory;

    private RecipeDetailsAdapter adapter;
    private RecipeDetailViewModel recipeDetailsViewModel;
    private SimpleExoPlayer player = null;
    private Uri videoUri = null;

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
        initRecyclerView(container.getContext());
        initViewModel();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoUri != null) {
            final DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            if (player == null) {
                player = initPlayer(bandwidthMeter);
            }
            if (player != null) {
                player.prepare(getVideoSource(videoUri, getActivity(), bandwidthMeter));
                player.setPlayWhenReady(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
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

    @Override
    public void onVideoViewToBePrepared(
            final PlayerView playerView,
            final Uri mediaUri) {

        final DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        if (player == null) {
            player = initPlayer(bandwidthMeter);
        }
        playerView.setPlayer(player);

        if (getActivity() != null) {
            videoUri = mediaUri;
            player.prepare(getVideoSource(videoUri, getActivity(), bandwidthMeter));
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onVideoViewToBeStopped() {
        if (player != null) {
            player.stop();
        }
    }

    private void initRecyclerView(final Context context) {
        adapter = new RecipeDetailsAdapter(
                new ArrayList<RecipeItem>(),
                RecipeDetailsAdapterItemsUtil.getRecipeDetailsHeaders(context),
                this, this);
        recipeStuffList.setAdapter(adapter);

        final StickyHeaderDecoration stickyHeaderDecoration = new StickyHeaderDecoration(adapter);
        recipeStuffList.addItemDecoration(stickyHeaderDecoration, 0);
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

    @Nullable
    private SimpleExoPlayer initPlayer(final DefaultBandwidthMeter bandwidthMeter) {
        final Context context = getActivity();
        if (context == null) {
            return null;
        }
        final TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        final TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        return ExoPlayerFactory.newSimpleInstance(context, trackSelector);
    }

    private MediaSource getVideoSource(
            final Uri mediaUri,
            final Context context,
            final DefaultBandwidthMeter bandwidthMeter) {

        final DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, BuildConfig.APPLICATION_ID), bandwidthMeter);
        final ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(dataSourceFactory);
        return factory.createMediaSource(mediaUri);
    }

    public interface Callback {

        void onStepClicked(Step step);

    }

}
