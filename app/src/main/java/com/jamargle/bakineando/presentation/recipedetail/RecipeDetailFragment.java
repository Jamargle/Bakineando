package com.jamargle.bakineando.presentation.recipedetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public final class RecipeDetailFragment extends BaseFragment<RecipeDetailFragment.Callback>
        implements RecipeDetailsAdapter.OnStepClickListener,
        RecipeDetailsAdapter.VideoItemListener,
        PlaybackPreparer {

    private static final String RECIPE_TO_SHOW = "key:RecipeDetailFragment_recipe_to_show";
    private static final String SAVED_SCROLL_POSITION = "key:RecipeDetailFragment_scroll_position";
    private static final String SAVED_VIDEO_ENDED = "key:RecipeDetailFragment_video_ended";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String KEY_AUTO_PLAY = "key:RecipeDetailFragment_auto_play";
    private static final String KEY_WINDOW = "key:RecipeDetailFragment_start_window";
    private static final String KEY_POSITION = "key:RecipeDetailFragment_start_position";

    @BindView(R.id.recipe_stuff_list) RecyclerView recipeStuffList;

    @Inject ViewModelFactory viewModelFactory;

    private RecipeDetailsAdapter adapter;
    private RecipeDetailViewModel recipeDetailsViewModel;
    private SimpleExoPlayer player = null;
    private WeakReference<PlayerView> playerView;
    private MediaSource mediaSource;
    private Uri videoUri = null;
    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;
    private boolean isVideoEnded = false;

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
        if (container != null) {
            initRecyclerView(container.getContext());
            initViewModel();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_SCROLL_POSITION)) {
                final int position = savedInstanceState.getInt(SAVED_SCROLL_POSITION);
                scrollToSavedScrollPosition(position);
            }
            isVideoEnded = savedInstanceState.getBoolean(SAVED_VIDEO_ENDED);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        final int position = ((LinearLayoutManager) recipeStuffList.getLayoutManager()).findLastVisibleItemPosition();
        if (position >= 0) {
            outState.putInt(SAVED_SCROLL_POSITION, position);
        }
        updateStartPosition();
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
        outState.putBoolean(SAVED_VIDEO_ENDED, isVideoEnded);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected boolean isToBeRetained() {
        // If we retain the instance of this fragment, during a configurationChange android tries to
        // put it in the same view (and for tablets layout it may be not possible) so do not retain
        // this fragment
        return false;
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

        this.playerView = new WeakReference<>(playerView);
        videoUri = mediaUri;
        initializePlayer();
    }

    @Override
    public void onVideoViewToBeStopped() {
        releasePlayer();
    }

    @Override
    public void preparePlayback() {
        initializePlayer();
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

    private void scrollToSavedScrollPosition(final int position) {
        final Handler handler = new Handler();
        final int delay = 200;  // Add some delay to perform the scroll after the view is initialized
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (RecyclerView.NO_POSITION != position) {
                    final LinearLayoutManager layoutManager =
                            (LinearLayoutManager) recipeStuffList.getLayoutManager();
                    layoutManager.smoothScrollToPosition(recipeStuffList, null, position);
                }
            }
        }, delay);
    }

    private void initializePlayer() {
        if (videoUri == null || playerView.get() == null) {
            return;
        }
        final Context context = getActivity();
        if (player == null && context != null) {
            player = ExoPlayerFactory.newSimpleInstance(context, createTrackSelector());
            player.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(
                        final boolean playWhenReady,
                        final int playbackState) {

                    switch (playbackState) {
                        case Player.STATE_ENDED:
                            isVideoEnded = true;
                            break;
                        default:
                            // Do nothing
                    }
                }

                @Override
                public void onPlayerError(final ExoPlaybackException error) {
                    if (isBehindLiveWindow(error)) {
                        clearStartPosition();
                        initializePlayer();
                    } else {
                        updateStartPosition();
                    }
                }
            });
            player.setPlayWhenReady(startAutoPlay);
            playerView.get().setPlayer(player);
            playerView.get().setPlaybackPreparer(this);
        }
        boolean haveStartPosition = startWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            if (player != null) {
                player.seekTo(startWindow, startPosition);
            }
        }
        if (player != null) {
            mediaSource = getVideoSource(videoUri, context);
            player.prepare(mediaSource, !haveStartPosition, false);
        }
    }

    private boolean isBehindLiveWindow(final ExoPlaybackException exception) {
        if (exception.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = exception.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private MediaSource getVideoSource(
            @NonNull final Uri mediaUri,
            final Context context) {

        final DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, BuildConfig.APPLICATION_ID), BANDWIDTH_METER);
        final ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(dataSourceFactory);
        return factory.createMediaSource(mediaUri);
    }

    private void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            player.release();
            player = null;
            mediaSource = null;
        }
    }

    private void updateStartPosition() {
        if (player != null) {
            startAutoPlay = player.getPlayWhenReady();
            startWindow = player.getCurrentWindowIndex();
            startPosition = Math.max(0, player.getContentPosition());
        }
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    private TrackSelector createTrackSelector() {
        final TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        return new DefaultTrackSelector(trackSelectionFactory);
    }

    public interface Callback {

        void onStepClicked(Step step);

    }

}
