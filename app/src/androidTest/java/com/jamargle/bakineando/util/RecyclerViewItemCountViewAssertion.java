package com.jamargle.bakineando.util;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public final class RecyclerViewItemCountViewAssertion implements ViewAssertion {

    private final int expectedCount;

    public RecyclerViewItemCountViewAssertion(final int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public void check(
            final View view,
            final NoMatchingViewException noViewFoundException) {

        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        final RecyclerView recyclerView = (RecyclerView) view;
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), is(expectedCount));
    }

}
