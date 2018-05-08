package com.jamargle.bakineando.presentation.recipelist;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.presentation.recipedetail.RecipeDetailActivity;
import com.jamargle.bakineando.presentation.recipedetail.RecipeDetailActivityTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.jamargle.bakineando.util.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    private static final String EXTRA_RECIPE_ID_KEY = "RECIPE_ID";
    private static final int EXTRA_RECIPE_ID_VALUE = 1;

    @Rule
    public ActivityTestRule<RecipeListActivity> testRule = new ActivityTestRule<>(RecipeListActivity.class);

    private IdlingResource idlingResource;

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Before
    public void registerIdlingResource() {
        idlingResource = testRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void listOfRecipesIsShown() {
        onView(withId(R.id.recipes_recycler_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void listItemsAreClickable() {
        final ViewInteraction recipeView = onView(withRecyclerView(R.id.recipes_recycler_view).atPosition(0));
        recipeView.check(matches(allOf(
                isDisplayed(),
                isClickable())));
    }

    @Test
    public void onRecipeClickNavigatesToRecipeDetails() {
        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        final Context context = InstrumentationRegistry.getTargetContext();

        Intents.init();
        intending(allOf(
                hasComponent(new ComponentName(context, RecipeDetailActivity.class)),
                hasExtra(RecipeDetailActivityTest.RECIPE_TO_SHOW, any(Recipe.class))));
        Intents.release();
    }

    @Test
    public void listItemsContainsIconNameAndNumberOfServings() {
        final Context context = InstrumentationRegistry.getTargetContext();
        final ViewInteraction iconView = onView(allOf(
                withId(R.id.recipe_icon),
                withContentDescription(context.getString(R.string.content_description_recipe_icon)),
                childAtPosition(allOf(
                        withId(R.id.recipe_item_container),
                        childAtPosition(
                                withId(R.id.recipes_recycler_view),
                                0)),
                        0)));
        iconView.check(matches(isDisplayed()));

        final ViewInteraction nameView = onView(allOf(
                withId(R.id.recipe_name),
                childAtPosition(allOf(
                        withId(R.id.recipe_item_container),
                        childAtPosition(
                                withId(R.id.recipes_recycler_view),
                                0)),
                        1)));
        nameView.check(matches(isDisplayed()));

        final ViewInteraction servingsView = onView(allOf(
                withId(R.id.recipe_servings),
                childAtPosition(allOf(
                        withId(R.id.recipe_item_container),
                        childAtPosition(
                                withId(R.id.recipes_recycler_view),
                                0)),
                        2)));
        servingsView.check(matches(isDisplayed()));
    }

}
