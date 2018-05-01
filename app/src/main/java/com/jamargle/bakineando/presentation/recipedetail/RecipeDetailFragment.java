package com.jamargle.bakineando.presentation.recipedetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.dummy.DummyContent;
import com.jamargle.bakineando.presentation.BaseFragment;

public final class RecipeDetailFragment extends BaseFragment<RecipeDetailFragment.Callback> {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    private static final String RECIPE_TO_SHOW = "key:RecipeDetailFragment_recipe_to_show";
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            final Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    public static RecipeDetailFragment newInstance(@NonNull final Recipe recipe) {
        final Bundle args = new Bundle();
        args.putParcelable(RECIPE_TO_SHOW, recipe);
        final RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) view.findViewById(R.id.recipe_detail)).setText(mItem.details);
        }

        return view;
    }

    @Override
    protected boolean isToBeRetained() {
        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.recipe_detail;
    }

    interface Callback {
    }

}
