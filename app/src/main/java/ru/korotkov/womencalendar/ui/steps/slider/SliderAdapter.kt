package ru.korotkov.womencalendar.ui.steps.slider

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.slider_item_row.view.*
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.inflate

    class SliderAdapter(private val array: IntArray) : RecyclerView.Adapter<SliderAdapter.SliderItemHolder>(){

    override fun onBindViewHolder(holder: SliderItemHolder, position: Int) {
        val itemSliderElement = array[position % array.size]
        holder.bindItem(itemSliderElement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderItemHolder {
        val inflatedView = parent.inflate(R.layout.slider_item_row)
        return SliderItemHolder(inflatedView)
    }

    override fun getItemCount() = Int.MAX_VALUE

    class SliderItemHolder(v: View): RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bindItem(element: Int) {
            view.itemName.text = element.toString()
        }
    }
}