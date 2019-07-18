package com.vio.calendar.model.date

class DateIterator(start: Date, private val endInclusive: Date) : Iterator<Date> {

    private var current = start

    private val lengthCycle =  28//UserInfoPreferences.getUserInfo().lengthCycle
    private val lengthMenstrualCycle = 5//UserInfoPreferences.getUserInfo().lengthMenstrual

    private var counter = 0

    override fun hasNext(): Boolean {
        return current <= endInclusive
    }

    override fun next(): Date {
        return current++
    }
}