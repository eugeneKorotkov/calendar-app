package com.vio.womencalendar.ui.today

/*
class TodayRecyclerAdapter(private val days: ArrayList<Day>): RecyclerView.Adapter<TodayRecyclerAdapter.DayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val inflatedView = parent.inflate(R.layout.day_item_row)
        return DayViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        holder.bindItem(day)
    }

    class DayViewHolder(v: View): RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bindItem(day: Day) {
            view.dayName.text = day.number.toString()

            when (day.type) {
                /*Day.Type.MENSTRUAL -> {
                    view.day_number.setTextColor(Color.WHITE)
                    view.dayLinear.setBackgroundResource(R.drawable.mes_back)
                }
                Day.Type.OVULATION -> {
                    view.day_number.setTextColor(Color.BLUE)
                }*/
            }
        }

    }
}
        */