package com.jamargle.bakineando.presentation.stepdetail;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Step;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StepDetailScreenTest {

    private static final String STEP_TO_SHOW = "param:step_to_show";

    private static final Step EXPECTED_STEP = new Step.Builder()
            .videoURL("")
            .thumbnailURL("")
            .build();

    @Rule
    public ActivityTestRule<StepDetailActivity> activityTestRule
            = new ActivityTestRule<>(StepDetailActivity.class, false, false);

    @Test
    public void toolbarHasStepNumberAsText() {
        final int stepNumber = 1;
        EXPECTED_STEP.setStepNumber(stepNumber);
        launchActivity();

        final String expectedToolbarLabel = InstrumentationRegistry.getTargetContext()
                .getString(R.string.step_number, stepNumber);

        final ViewInteraction view = onView(
                allOf(IsInstanceOf.instanceOf(AppCompatTextView.class),
                      withParent(withId(R.id.action_bar)),
                      isDisplayed()));
        view.check(matches(allOf(
                withText(expectedToolbarLabel),
                isDisplayed())));
    }

    @Test
    public void shortDescriptionIsShown() {
        final String shortDescription = "some short description";
        EXPECTED_STEP.setShortDescription(shortDescription);
        launchActivity();

        final ViewInteraction view = onView(allOf(
                withId(R.id.short_description),
                IsInstanceOf.instanceOf(TextView.class),
                isDisplayed()));
        view.check(matches(allOf(
                withText(shortDescription),
                isDisplayed())));
    }

    @Test
    public void fullDescriptionIsShown() {
        final String fullDescription = "some full description";
        EXPECTED_STEP.setDescription(fullDescription);
        launchActivity();

        final ViewInteraction view = onView(allOf(
                withId(R.id.full_description),
                IsInstanceOf.instanceOf(TextView.class),
                isDisplayed()));
        view.check(matches(allOf(
                withText(fullDescription),
                isDisplayed())));
    }

    @Test
    public void videoIsShown() {
        final String videoUrl = "some url for a video";
        EXPECTED_STEP.setVideoURL(videoUrl);
        launchActivity();

        final ViewInteraction videoView = onView(allOf(
                withId(R.id.step_video),
                IsInstanceOf.instanceOf(PlayerView.class),
                isDisplayed()));
        videoView.check(matches(isDisplayed()));

        // When there is video, no image is shown
        final ViewInteraction imageView = onView(allOf(
                withId(R.id.step_image),
                IsInstanceOf.instanceOf(ImageView.class)));
        imageView.check(matches(not(isDisplayed())));
    }

    private void launchActivity() {
        final Intent intent = new Intent();
        intent.putExtra(STEP_TO_SHOW, EXPECTED_STEP);
        activityTestRule.launchActivity(intent);
    }

}
