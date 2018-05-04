package com.jamargle.bakineando.presentation.stepdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.di.ViewModelFactory;
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.presentation.BaseFragment;
import javax.inject.Inject;

public final class StepDetailFragment extends BaseFragment<StepDetailFragment.Callback> {

    private static final String STEP_TO_SHOW = "key:StepDetailFragment_step_to_show";
    @Inject ViewModelFactory viewModelFactory;

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
    protected boolean isToBeRetained() {
        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_step_details;
    }

    public interface Callback {

    }

}
