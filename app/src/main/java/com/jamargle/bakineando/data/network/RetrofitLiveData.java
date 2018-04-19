package com.jamargle.bakineando.data.network;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class RetrofitLiveData<T> extends LiveData<T> implements Callback<T> {

    private final Call<T> call;

    RetrofitLiveData(final Call<T> call) {
        this.call = call;
    }

    @Override
    protected void onActive() {
        if (!call.isCanceled() && !call.isExecuted()) {
            call.enqueue(this);
        }
    }

    @Override
    public void onResponse(
            @NonNull final Call<T> call,
            @NonNull final Response<T> response) {

        postValue(response.body());
    }

    @Override
    public void onFailure(
            @NonNull final Call<T> call,
            @NonNull final Throwable t) {
        // Do nothing
    }

}
