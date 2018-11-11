package com.example.smalltalkAndroid.feature

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView


class ItemSpacer(private val context: Context, @DimenRes private val decorationHeight: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        val decHeight = context.resources.getDimensionPixelSize(decorationHeight)
        outRect.right = decHeight * 3
        outRect.left = decHeight * 3
        outRect.bottom = decHeight
        if (itemPosition == 0)
            outRect.top = decHeight * 2
    }
}