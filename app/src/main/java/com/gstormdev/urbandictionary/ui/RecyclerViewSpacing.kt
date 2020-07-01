package com.gstormdev.urbandictionary.ui

import android.graphics.Rect
import android.view.View
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSpacing(@IntRange(from = 0) val margin: Int, @IntRange(from = 0) val columns: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position  = parent.getChildLayoutPosition(view)
        // Set right margin to all
        outRect.right = margin

        // Set bottom margin to all
        outRect.bottom = margin

        // Only add top margin to the first row
        if (position < columns) {
            outRect.top = margin
        }

        // Only add left margin to the first column
        if (position % columns == 0) {
            outRect.left = margin
        }
    }
}