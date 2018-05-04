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
import android.widget.TextView;
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
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.presentation.BaseFragment;
import javax.inject.Inject;

public final class StepDetailFragment extends BaseFragment<StepDetailFragment.Callback> {

    private static final String STEP_TO_SHOW = "key:StepDetailFragment_step_to_show";

    @BindView(R.id.short_description) TextView shortDescriptionView;
    @BindView(R.id.step_video) PlayerView stepVideoView;
    @BindView(R.id.full_description) TextView fullDescriptionView;

    @Inject ViewModelFactory viewModelFactory;

    private StepDetailViewModel stepDetailsViewModel;
    private SimpleExoPlayer player = null;
    private Uri videoUri = null;

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
        return R.layout.fragment_step_details;
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

                        final DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                        if (player == null) {
                            player = initPlayer(bandwidthMeter);
                        }
                        stepVideoView.setPlayer(player);

                        if (getActivity() != null) {
                            player.prepare(getVideoSource(videoUri, getActivity(), bandwidthMeter));
                            player.setPlayWhenReady(true);
                        }
                    } else {
                        stepVideoView.setVisibility(View.GONE);
                    }
                }
            }

        });

        if (getArguments() != null) {
            stepDetailsViewModel.setStep((Step) getArguments().getParcelable(STEP_TO_SHOW));
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

    }

}
