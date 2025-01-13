package com.example.testcalendar.custom_calendar

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.testcalendar.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

internal class DatePickerCalendarAdapter(
    private val mContext: Context,
    datePickerManagerImpl: DatePickerManager
): PagerAdapter() {
    private lateinit var currentCalendarMonth: Calendar
    private lateinit var llTitleWeekContainer: ConstraintLayout
    private lateinit var llDaysContainer: LinearLayout

    private val mDatePickerManager: DatePickerManager

    private val mOnDateClickListener = object : DateView.OnDateClickListener {
        override fun onDateClicked(view: View, selectedDate: Calendar) {
            setSelectedDate(selectedDate)
        }
    }

    init {
        mDatePickerManager = datePickerManagerImpl
    }

    override fun getCount(): Int {
        return mDatePickerManager.getVisibleMonDataList().size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val modelObject = mDatePickerManager.getVisibleMonDataList()[position]
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.item_pager_month, container, false) as ViewGroup

        initBindingView(layout)

        drawCalendarForMonth(getCurrentMonth(modelObject))
        container.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    private fun initBindingView(layout: ViewGroup) {
        llTitleWeekContainer = layout.findViewById(R.id.llTitleWeekContainer)
        llDaysContainer = layout.findViewById(R.id.llDaysContainer)
    }

    private fun drawCalendarForMonth(month: Calendar) {
        currentCalendarMonth = month.clone() as Calendar
        currentCalendarMonth[Calendar.DATE] = 1
        resetTime(currentCalendarMonth, DateTiming.NONE)
        val weekTitle = mContext.resources.getStringArray(R.array.week_sun_sat)
        // To set week day title as per offset
        for (i in 0 until 7) {
            val textView = llTitleWeekContainer.getChildAt(i) as AppCompatTextView
            val weekStr = weekTitle[(i + 1) % 7]
            textView.text = weekStr
        }
        var startDay = month[Calendar.DAY_OF_WEEK] - 1

        //To rotate week day according to offset
        if (startDay < 1) {
            startDay += 7
        }

        month.add(Calendar.DATE, -startDay + 1)
        for (i in 0 until llDaysContainer.childCount) {
            val weekRow = llDaysContainer.getChildAt(i) as ConstraintLayout
            for (j in 0 until 7) {
                val customDateView = weekRow.getChildAt(j) as CustomDateView
                drawDayContainer(customDateView, month)
                month.add(Calendar.DATE, 1)
            }
        }
    }

    private fun drawDayContainer(customDateView: CustomDateView, date: Calendar) {
        customDateView.setDateText(date[Calendar.DATE].toString())
        customDateView.setDateClickListener(mOnDateClickListener)
        val dateState: DateView.DateState = if (currentCalendarMonth[Calendar.MONTH] != date[Calendar.MONTH]) {
            DateView.DateState.HIDDEN
        } else {
            val type = mDatePickerManager.checkDate(date)
            val a = type === DateView.DateSelectionState.SELECTED
            Log.e(">>>>","a: $a")
            if (type === DateView.DateSelectionState.SELECTED) {
                DateView.DateState.SELECTED
            } else {
                if (mDatePickerManager.isSelectableDate(date)) {
                    DateView.DateState.SELECTABLE
                } else {
                    DateView.DateState.DISABLE
                }
            }
        }
        customDateView.updateDateBackground(dateState)
        customDateView.tag = getContainerKey(date)
    }

    private fun getContainerKey(cal: Calendar): Long {
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
        val str = simpleDateFormat.format(cal.time)
        return str.toLong()
    }

    private fun getCurrentMonth(calendar: Calendar): Calendar {
        val current = calendar.clone() as Calendar
        current[Calendar.DAY_OF_MONTH] = 1
        return current
    }

    fun setSelectedDate(selectedDate: Calendar) {
        Log.e(">>>>", "selectedDate: ${selectedDate.time.date}")
        mDatePickerManager.setSelectedDate(selectedDate)
        drawCalendarForMonth(currentCalendarMonth)
    }
}