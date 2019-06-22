package ru.korotkov.womencalendar.ui.steps.slider

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SliderLayoutManager(context: Context): LinearLayoutManager(context) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        scaleDownView()
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {

        if (orientation == VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dy, recycler, state)
            scaleDownView()
            return scrolled

        } else {

            return 0
        }
    }

    private fun scaleDownView() {

        val mid = height / 2.0f

        for (i in 0 until childCount) {

            val child = getChildAt(i)

            if (child != null) {
                val childMid = (getDecoratedTop(child) + getDecoratedBottom(child)) / 2.0f
                val distanceFromCenter = Math.abs(mid - childMid)
                val scale = 1 - Math.sqrt((distanceFromCenter/height).toDouble()).toFloat()*0.66f

                child.scaleX = scale
                child.scaleY = scale
            }

        }
    }
}