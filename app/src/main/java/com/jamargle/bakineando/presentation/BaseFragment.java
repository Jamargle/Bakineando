package com.jamargle.bakineando.presentation;

import android.app.Activity;
import android.support.v4.app.Fragment;

import dagger.android.support.AndroidSupportInjection;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(final Activity activity) {
        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

}
