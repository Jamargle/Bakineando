package com.jamargle.bakineando.presentation.stepdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.jamargle.bakineando.domain.model.Step;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class StepDetailViewModel extends ViewModel {

    private final MutableLiveData<Step> stepLiveData = new MutableLiveData<>();

    @Inject
    public StepDetailViewModel() {
    }

    LiveData<Step> getStep() {
        return stepLiveData;
    }

    void setStep(final Step stepToShow) {
        stepLiveData.setValue(stepToShow);
    }

}
