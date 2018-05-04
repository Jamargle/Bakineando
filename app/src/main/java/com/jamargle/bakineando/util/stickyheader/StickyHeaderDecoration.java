package com.jamargle.bakineando.util.stickyheader;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * A sticky header decoration for android's RecyclerView.
 */
public final class StickyHeaderDecoration extends RecyclerView.ItemDecoration {

    private static final long NO_HEADER_ID = -1L;

    private LongSparseArray<RecyclerView.ViewHolder> headerCache;
    private StickyHeaderAdapter adapter;
    private boolean renderInline;

    /**
     * @param adapter the sticky header adapter to use
     */
    public StickyHeaderDecoration(final StickyHeaderAdapter adapter) {
        this(adapter, false);
    }

    /**
     * @param adapter the sticky header adapter to use
     */
    private StickyHeaderDecoration(
            final StickyHeaderAdapter adapter,
            final boolean renderInline) {

        this.adapter = adapter;
        headerCache = new LongSparseArray<>();
        this.renderInline = renderInline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getItemOffsets(
            final Rect outRect,
            final View view,
            final RecyclerView parent,
            final RecyclerView.State state) {

        final int position = parent.getChildAdapterPosition(view);
        int headerHeight = 0;

        if (position != RecyclerView.NO_POSITION
                && hasHeader(position)
                && showHeaderAboveItem(position)) {

            final View header = getHeader(parent, position).itemView;
            headerHeight = getHeaderHeightForLayout(header);
        }

        outRect.set(0, headerHeight, 0, 0);
    }

    private boolean hasHeader(final int position) {
        return adapter.getHeaderId(position) != NO_HEADER_ID;
    }

    private boolean showHeaderAboveItem(final int itemAdapterPosition) {
        return itemAdapterPosition == 0 || adapter.getHeaderId(itemAdapterPosition - 1) != adapter.getHeaderId(itemAdapterPosition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDrawOver(
            final Canvas canvas,
            final RecyclerView parent,
            final RecyclerView.State state) {

        final int count = parent.getChildCount();
        long previousHeaderId = -1;

        for (int layoutPos = 0; layoutPos < count; layoutPos++) {
            final View child = parent.getChildAt(layoutPos);
            final int adapterPos = parent.getChildAdapterPosition(child);

            if (adapterPos != RecyclerView.NO_POSITION && hasHeader(adapterPos)) {
                long headerId = adapter.getHeaderId(adapterPos);

                if (headerId != previousHeaderId) {
                    previousHeaderId = headerId;
                    View header = getHeader(parent, adapterPos).itemView;
                    canvas.save();

                    final int left = child.getLeft();
                    final int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);
                    canvas.translate(left, top);

                    header.setTranslationX(left);
                    header.setTranslationY(top);
                    header.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }

    private int getHeaderTop(
            final RecyclerView parent,
            final View child,
            final View header,
            final int adapterPos,
            final int layoutPos) {

        final int headerHeight = getHeaderHeightForLayout(header);
        int top = ((int) child.getY()) - headerHeight;
        if (layoutPos == 0) {
            final int count = parent.getChildCount();
            final long currentId = adapter.getHeaderId(adapterPos);
            // find next view with header and compute the offscreen push if needed
            for (int i = 1; i < count; i++) {
                final int adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i));
                if (adapterPosHere != RecyclerView.NO_POSITION) {
                    final long nextId = adapter.getHeaderId(adapterPosHere);
                    if (nextId != currentId) {
                        final View next = parent.getChildAt(i);
                        final int offset = ((int) next.getY()) - (headerHeight + getHeader(parent, adapterPosHere).itemView.getHeight());
                        if (offset < 0) {
                            return offset;
                        } else {
                            break;
                        }
                    }
                }
            }

            top = Math.max(0, top);
        }

        return top;
    }

    private RecyclerView.ViewHolder getHeader(
            final RecyclerView parent,
            final int position) {

        final long key = adapter.getHeaderId(position);

        if (headerCache.get(key) != null) {
            return headerCache.get(key);
        } else {
            final RecyclerView.ViewHolder holder = adapter.onCreateHeaderViewHolder(parent);
            final View header = holder.itemView;

            //noinspection unchecked
            adapter.onBindHeaderViewHolder(holder, position);

            final int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.UNSPECIFIED);

            final int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                                                                 parent.getPaddingLeft() + parent.getPaddingRight(), header.getLayoutParams().width);
            final int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                                                                  parent.getPaddingTop() + parent.getPaddingBottom(),
                                                                  header.getLayoutParams().height);

            header.measure(childWidth, childHeight);
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());

            headerCache.put(key, holder);

            return holder;
        }
    }

    private int getHeaderHeightForLayout(final View header) {
        return renderInline ? 0 : header.getHeight();
    }

}
