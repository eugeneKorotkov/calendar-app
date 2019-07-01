package com.vio.womencalendar.model.date

class DateIterator(start: Date, private val endInclusive: Date) : Iterator<Date> {
    private var current = start

    override fun hasNext(): Boolean {
        return current <= endInclusive
    }

    override fun next(): Date {
        return current++
    }
}