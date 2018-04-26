package com.jamargle.bakineando.data;

import com.jamargle.bakineando.domain.interactor.PostExecutionThread;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * MainThread (UI Thread) implementation based on a {@link Scheduler} which will execute actions
 * on the Android UI thread
 */
public final class UiThread implements PostExecutionThread {

    public UiThread() {
    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
