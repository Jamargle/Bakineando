package com.jamargle.bakineando.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import dagger.android.support.AndroidSupportInjection;

public abstract class BaseFragment<C> extends Fragment {

    protected C callback;

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(final Activity activity) {
        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
        try {
            callback = (C) activity;
        } catch (final ClassCastException exception) {
            throw new RuntimeException(activity.toString() + " must implement " +
                    callback.getClass().getSimpleName());
        }
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(isToBeRetained());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    /**
     * Control whether a fragment instance is retained across Activity re-creation (such as from
     * a configuration change)
     */
    protected abstract boolean isToBeRetained();

}
