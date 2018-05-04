package com.jamargle.bakineando.util.stickyheader;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * The adapter to assist the {@link StickyHeaderDecoration} in creating and binding the header views.
 *
 * @param <T> the header view holder
 *            Created by Jose on 09/07/2017.
 */
public interface StickyHeaderAdapter<T extends RecyclerView.ViewHolder> {

    /**
     * Returns the header id for the item at the given position.
     *
     * @param position the item position
     * @return the header id
     */
    long getHeaderId(final int position);

    /**
     * Creates a new header ViewHolder.
     *
     * @param parent the header's view parent
     * @return a view holder for the created view
     */
    T onCreateHeaderViewHolder(final ViewGroup parent);

    /**
     * Updates the header view to reflect the header data for the given position
     *
     * @param viewHolder the header view holder
     * @param position   the header's item position
     */
    void onBindHeaderViewHolder(
            final T viewHolder,
            final int position);

}
