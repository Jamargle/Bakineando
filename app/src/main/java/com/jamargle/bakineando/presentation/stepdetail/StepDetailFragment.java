package com.jamargle.bakineando.presentation.stepdetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.presentation.BaseFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;

public final class StepDetailFragment extends BaseFragment<StepDetailFragment.Callback>
        implements PlaybackPreparer {

    private static final String STEP_TO_SHOW = "key:StepDetailFragment_step_to_show";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String KEY_AUTO_PLAY = "key:StepDetailFragment_auto_play";
    private static final String KEY_WINDOW = "key:StepDetailFragment_start_window";
    private static final String KEY_POSITION = "key:StepDetailFragment_start_position";

    @BindView(R.id.short_description) TextView shortDescriptionView;
    @BindView(R.id.step_image) ImageView stepImage;
    @BindView(R.id.step_video) PlayerView stepVideoView;
    @BindView(R.id.full_description) TextView fullDescriptionView;

    @Inject ViewModelFactory viewModelFactory;

    private StepDetailViewModel stepDetailsViewModel;
    private SimpleExoPlayer player = null;
    private MediaSource mediaSource;
    private Uri videoUri = null;
    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;

    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance(@NonNull final Step step) {
        final Bundle args = new Bundle();
        args.putParcelable(STEP_TO_SHOW, step);
        final StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);
        initViewModel();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            clearStartPosition();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        updateStartPosition();
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoUri != null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected boolean isToBeRetained() {
        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_step_details;
    }

    @Override
    public void preparePlayback() {
        initializePlayer();
    }

    private void initViewModel() {
        stepDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(StepDetailViewModel.class);
        stepDetailsViewModel.getStep().observe(this, new Observer<Step>() {

            @Override
            public void onChanged(@Nullable Step step) {
                if (step != null) {
                    shortDescriptionView.setText(step.getShortDescription());
                    fullDescriptionView.setText(step.getDescription());

                    if (!step.getVideoURL().isEmpty()) {
                        videoUri = Uri.parse(step.getVideoURL());
                        initializePlayer();

                    } else {
                        stepVideoView.setVisibility(View.GONE);

                        if (!step.getThumbnailURL().isEmpty()
                                && (step.getThumbnailURL().endsWith(".jpg")
                                || step.getThumbnailURL().endsWith(".png"))) {

                            stepImage.setVisibility(View.VISIBLE);

                            Picasso.with(getActivity())
                                    .load(step.getThumbnailURL())
                                    .into(stepImage);
                        }
                    }
                }
            }

        });

        if (getArguments() != null) {
            stepDetailsViewModel.setStep((Step) getArguments().getParcelable(STEP_TO_SHOW));
        }
    }

    private void initializePlayer() {
        if (videoUri == null) {
            return;
        }
        final Context context = getActivity();
        if (player == null && context != null) {
            player = ExoPlayerFactory.newSimpleInstance(context, createTrackSelector());
            player.addListener(new Player.DefaultEventListener() {
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
            stepVideoView.setPlayer(player);
            stepVideoView.setPlaybackPreparer(this);
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

    }

}
