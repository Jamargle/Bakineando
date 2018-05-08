package com.jamargle.bakineando.presentation.recipelist;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public final class RecipesIdlingResource implements IdlingResource {

    private final AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Nullable
    private volatile ResourceCallback callback;

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(final ResourceCallback callback) {
        this.callback = callback;
    }

    public void setIdleState(final boolean idleState) {
        isIdleNow.set(idleState);
        final ResourceCallback callback = this.callback;
        if (idleState && callback != null) {
            callback.onTransitionToIdle();
        }
    }

}
