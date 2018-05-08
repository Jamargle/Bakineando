package com.jamargle.bakineando.presentation.recipedetail;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatTextView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.presentation.stepdetail.StepDetailActivity;
import com.jamargle.bakineando.presentation.stepdetail.StepDetailScreenTest;
import com.jamargle.bakineando.util.IngredientListUtil;
import com.jamargle.bakineando.util.RecyclerViewItemCountViewAssertion;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jamargle.bakineando.util.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    public static final String RECIPE_TO_SHOW = "param:recipe_to_show";

    private final Recipe expectedRecipe = new Recipe.Builder()
            .name("")
            .steps(new ArrayList<Step>())
            .ingredients(new ArrayList<Ingredient>())
            .build();

    @Rule
    public ActivityTestRule<RecipeDetailActivity> activityTestRule
            = new ActivityTestRule<>(RecipeDetailActivity.class, false, false);

    @Test
    public void toolbarHasRecipeNameAsText() {
        final String recipeName = "The beautiful recipe name";
        expectedRecipe.setName(recipeName);
        launchActivity();

        final ViewInteraction view = onView(
                allOf(IsInstanceOf.instanceOf(AppCompatTextView.class),
                      withParent(withId(R.id.action_bar)),
                      isDisplayed()));
        view.check(matches(allOf(
                withText(recipeName),
                isDisplayed())));
    }

    @Test
    public void listHasIntroductionNonClickableItem() {
        final List<Step> steps = new ArrayList<>();
        final Step introductionStep = new Step.Builder().shortDescription("Recipe Introduction").build();
        steps.add(introductionStep);
        expectedRecipe.setSteps(steps);
        launchActivity();

        checkListHasThisNumberOfItems(1);

        final ViewInteraction introductionStepView = onView(allOf(
                IsInstanceOf.instanceOf(PlayerView.class),
                withId(R.id.introduction_video)));
        introductionStepView.check(matches(isDisplayed()));
        introductionStepView.perform(click())
                .check(matches(not(isClickable())));
    }

    @Test
    public void listHasNotIntroductionItem() {
        final List<Step> steps = new ArrayList<>();
        final Step sampleStep = new Step.Builder().build();
        steps.add(sampleStep);
        steps.add(sampleStep);
        expectedRecipe.setSteps(steps);
        launchActivity();

        checkListHasThisNumberOfItems(2);

        final ViewInteraction introductionStepView = onView(allOf(
                IsInstanceOf.instanceOf(PlayerView.class),
                withId(R.id.introduction_video)));
        introductionStepView.check(doesNotExist());
    }

    @Test
    public void listHasIngredientItems() {
        final List<Ingredient> ingredients = new ArrayList<>();
        final String name1 = "Ingredient 1";
        final float quantity1 = 12.3f;
        final String measure1 = "measure 1";
        final Ingredient sampleIngredient1 = new Ingredient.Builder()
                .ingredient(name1)
                .quantity(quantity1).measure(measure1)
                .build();
        ingredients.add(sampleIngredient1);
        final String name2 = "Ingredient 2";
        final float quantity2 = 34.5f;
        final String measure2 = "measure 2";
        final Ingredient sampleIngredient2 = new Ingredient.Builder()
                .ingredient(name2)
                .quantity(quantity2).measure(measure2)
                .build();
        ingredients.add(sampleIngredient2);
        expectedRecipe.setIngredients(ingredients);
        launchActivity();

        checkListHasThisNumberOfItems(2);
        final Context context = InstrumentationRegistry.getTargetContext();

        final ViewInteraction ingredient1View = onView(withRecyclerView(R.id.recipe_stuff_list).atPosition(0));
        ingredient1View.check(matches(allOf(
                isDisplayed(),
                hasDescendant(withText(name1)),
                hasDescendant(withText(IngredientListUtil.getIngredientQuantityLabel(context, quantity1, measure1))))));

        final ViewInteraction ingredient2View = onView(withRecyclerView(R.id.recipe_stuff_list).atPosition(1));
        ingredient2View.check(matches(allOf(
                isDisplayed(),
                hasDescendant(withText(name2)),
                hasDescendant(withText(IngredientListUtil.getIngredientQuantityLabel(context, quantity2, measure2))))));
    }

    @Test
    public void listIngredientAreNotClickable() {
        final List<Ingredient> ingredients = new ArrayList<>();
        final Ingredient sampleIngredient = new Ingredient.Builder().build();
        ingredients.add(sampleIngredient);
        expectedRecipe.setIngredients(ingredients);
        launchActivity();

        checkListHasThisNumberOfItems(1);
        final ViewInteraction ingredientView = onView(withRecyclerView(R.id.recipe_stuff_list).atPosition(0));
        ingredientView.perform(click())
                .check(matches(not(isClickable())));
    }

    @Test
    public void listHasStepItems() {
        final List<Step> steps = new ArrayList<>();
        final int stepNumber1 = 123;
        final String stepShortDescription1 = "abc";
        final String expectedStepViewLabel1 = stepNumber1 + " " + stepShortDescription1;
        final Step sampleStep1 = new Step.Builder().stepNumber(stepNumber1).shortDescription(stepShortDescription1).build();
        steps.add(sampleStep1);
        final int stepNumber2 = 456;
        final String stepShortDescription2 = "def";
        final String expectedStepViewLabel2 = stepNumber2 + " " + stepShortDescription2;
        final Step sampleStep2 = new Step.Builder().stepNumber(stepNumber2).shortDescription(stepShortDescription2).build();
        steps.add(sampleStep2);
        expectedRecipe.setSteps(steps);
        launchActivity();

        checkListHasThisNumberOfItems(2);

        final ViewInteraction step1View = onView(withRecyclerView(R.id.recipe_stuff_list).atPosition(0));
        step1View.check(matches(allOf(
                isDisplayed(),
                hasDescendant(withText(expectedStepViewLabel1)))));

        final ViewInteraction step2View = onView(withRecyclerView(R.id.recipe_stuff_list).atPosition(1));
        step2View.check(matches(allOf(
                isDisplayed(),
                hasDescendant(withText(expectedStepViewLabel2)))));
    }

    @Test
    public void listStepsAreClickable() {
        final List<Step> steps = new ArrayList<>();
        final int stepNumber1 = 123;
        final String stepShortDescription1 = "abc";
        final String videoUrl = "some video url.mp4";
        final Step sampleStep1 = new Step.Builder().videoURL(videoUrl).stepNumber(stepNumber1).shortDescription(stepShortDescription1)
                .build();
        steps.add(sampleStep1);
        expectedRecipe.setSteps(steps);
        launchActivity();

        checkListHasThisNumberOfItems(1);

        final ViewInteraction stepView = onView(withRecyclerView(R.id.recipe_stuff_list).atPosition(0));
        stepView.check(matches(isClickable()));
        stepView.perform(click());

        final Context context = InstrumentationRegistry.getTargetContext();

        Intents.init();
        intending(allOf(
                hasComponent(new ComponentName(context, StepDetailActivity.class)),
                hasExtra(StepDetailScreenTest.STEP_TO_SHOW, sampleStep1)));
        Intents.release();
    }

    private void launchActivity() {
        final Intent intent = new Intent();
        intent.putExtra(RECIPE_TO_SHOW, expectedRecipe);
        activityTestRule.launchActivity(intent);
    }

    private void checkListHasThisNumberOfItems(final int numberOfItems) {
        onView(withId(R.id.recipe_stuff_list))
                .check(new RecyclerViewItemCountViewAssertion(numberOfItems));
    }

}
