package com.example.testcalendar.custom_calendar

import android.util.Log
import java.util.Calendar

internal class DatePickerManager(
    startMonthDate: Calendar,
    endMonthDate: Calendar,
    startSelectableDate: Calendar,
    endSelectableDate: Calendar,
) {
    private lateinit var mStartVisibleMonth: Calendar
    private lateinit var mEndVisibleMonth: Calendar
    private val mVisibleMonths = mutableListOf<Calendar>()
    private var mSelectedDate: Calendar? = null

    private var initStartSelectableDate = startSelectableDate.clone() as Calendar
    private var initEndSelectableDate = endSelectableDate.clone() as Calendar
    private lateinit var mStartSelectableDate: Calendar
    private lateinit var mEndSelectableDate: Calendar

    init {
        setVisibleMonth(startMonthDate, endMonthDate)
    }

    fun getVisibleMonDataList(): List<Calendar> {
        return mVisibleMonths
    }

    private fun setVisibleMonth(
        startMonth: Calendar,
        endMonth: Calendar
    ) {
        validateDatesOrder(startMonth, endMonth)
        val startMonthDate = startMonth.clone() as Calendar
        val endMonthDate = endMonth.clone() as Calendar
        resetTime(startMonthDate, DateTiming.START)
        resetTime(endMonthDate, DateTiming.END)

        mStartVisibleMonth = startMonthDate.clone() as Calendar
        resetTime(mStartVisibleMonth, DateTiming.START)
        mEndVisibleMonth = endMonthDate.clone() as Calendar
        resetTime(mEndVisibleMonth, DateTiming.END)

        mVisibleMonths.clear()
        val temp = mStartVisibleMonth.clone() as Calendar
        while (!isMonthSame(temp, mEndVisibleMonth)) {
            mVisibleMonths.add(temp.clone() as Calendar)
            temp.add(Calendar.MONTH, 1)
        }
        mVisibleMonths.add(temp.clone() as Calendar)

        setSelectableDateRange(initStartSelectableDate, initEndSelectableDate)
    }

    fun setSelectedDate(selectedDate: Calendar) {
        this.mSelectedDate = selectedDate
    }

    private fun resetSelectedDate() {
        this.mSelectedDate = null
    }

    fun checkDate(selectedDate: Calendar): DateView.DateSelectionState {
        if (mSelectedDate != null) {
            Log.e(">>>>", "mSelectedDate: ${mSelectedDate?.time?.date} - selectedDate: ${selectedDate.time.date}")
            val isDateSame = isDateSame(mSelectedDate!!, selectedDate)
            Log.e(">>>>", "isDateSame: $isDateSame")
            if (isDateSame(mSelectedDate!!, selectedDate)) {
                return DateView.DateSelectionState.SELECTED
            }
        }
        return DateView.DateSelectionState.UNKNOWN
    }

    private fun setSelectableDateRange(startDate: Calendar, endDate: Calendar) {
        validateDatesOrder(startDate, endDate)
        mStartSelectableDate = startDate.clone() as Calendar
        Log.e(">>>>", "startSelectableDate: ${startDate.time}")

        resetTime(mStartSelectableDate, DateTiming.START)
        mEndSelectableDate = endDate.clone() as Calendar
        resetTime(mEndSelectableDate, DateTiming.END)
        if (mStartSelectableDate.before(mStartVisibleMonth)) {
            throw InvalidDateException(
                "Selectable start date ${printDate(startDate)} is out of visible months" +
                        "(${printDate(mStartVisibleMonth)} " +
                        "- ${printDate(mEndVisibleMonth)})."
            )
        }
        if (mEndSelectableDate.after(mEndVisibleMonth)) {
            throw InvalidDateException(
                "Selectable end date ${printDate(endDate)} is out of visible months" +
                        "(${printDate(mStartVisibleMonth)} " +
                        "- ${printDate(mEndVisibleMonth)})."
            )
        }
        resetSelectedDate()
    }

    fun isSelectableDate(date: Calendar): Boolean {
        val isSelectable = !(date.before(mStartSelectableDate) || date.after(mEndSelectableDate))
        if (!(!isSelectable && checkDate(date) !== DateView.DateSelectionState.UNKNOWN)) {
            "Selected date can not be out of Selectable Date range." +
                    " Date: ${printDate(date)}" +
                    " Selected Date: ${printDate(mSelectedDate)}"
        }
        return isSelectable
    }

    private fun validateDatesOrder(start: Calendar, end: Calendar?) {
        if (start.after(end)) {
            throw Exception("Start date(${printDate(start)}) can not be after end date(${printDate(end)}).")
        }
    }
}