package com.jamargle.bakineando.presentation;

import android.app.Activity;
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
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

}
