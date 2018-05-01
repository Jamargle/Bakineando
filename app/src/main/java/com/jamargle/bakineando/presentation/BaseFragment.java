package com.jamargle.bakineando.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public abstract class BaseFragment<C> extends Fragment {

    protected C callback;

    @SuppressWarnings({"unchecked", "deprecation"})
    @Override
    public void onAttach(final Activity activity) {
        if (((BaseActivity) activity).supportFragmentInjector() != null) {
            AndroidSupportInjection.inject(this);
        }
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
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(getLayoutResourceId(), container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
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

    protected abstract @LayoutRes int getLayoutResourceId();

}
