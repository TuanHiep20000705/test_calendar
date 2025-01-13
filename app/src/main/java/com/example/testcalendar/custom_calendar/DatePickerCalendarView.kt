package com.example.testcalendar.custom_calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.testcalendar.databinding.LayoutDatePickerCalendarViewBinding
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@SuppressLint("ViewConstructor")
class DatePickerCalendarView (
    context: Context,
    attrs: AttributeSet?,

) : ConstraintLayout(context, attrs) {
    private var binding: LayoutDatePickerCalendarViewBinding
    private var mDatePickerManager: DatePickerManager
    private var datePickerCalendarAdapter: DatePickerCalendarAdapter
    private var locale: Locale = context.resources.configuration.locale

    init {
        binding = LayoutDatePickerCalendarViewBinding.inflate(LayoutInflater.from(context), this, true)

        val defStartMonth = Calendar.getInstance().clone() as Calendar
        defStartMonth.add(Calendar.MONTH, -2)
        val defEndMonth = Calendar.getInstance().clone() as Calendar
        defEndMonth.add(Calendar.MONTH, 2)
        val endDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            timeZone = TimeZone.getTimeZone("GMT+07:00")
        }
        mDatePickerManager = DatePickerManager(defStartMonth, defEndMonth, defStartMonth, endDate)
        datePickerCalendarAdapter = DatePickerCalendarAdapter(context, mDatePickerManager)
        binding.apply {
            vpCalendar.adapter = datePickerCalendarAdapter
            vpCalendar.offscreenPageLimit = 0
            vpCalendar.currentItem = 2
        }
        setCalendarYearTitle(2)
        setListener()
    }

    private fun setListener() {
        binding.vpCalendar.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit
            override fun onPageSelected(position: Int) {
                setCalendarYearTitle(position)
                setNavigationHeader(position)
            }
            override fun onPageScrollStateChanged(state: Int) = Unit
        })
        binding.imgNavLeft.setOnClickListener {
            binding.vpCalendar.currentItem -= 1
        }
        binding.imgNavRight.setOnClickListener {
            binding.vpCalendar.currentItem += 1
        }
    }

    private fun setCalendarYearTitle(position: Int) {
        val currentCalendarMonth = mDatePickerManager.getVisibleMonDataList()[position]
        var dateText = DateFormatSymbols(locale).months[currentCalendarMonth[Calendar.MONTH]]
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length)
        val yearTitle = dateText + " " + currentCalendarMonth[Calendar.YEAR]
        binding.tvYearTitle.text = yearTitle
    }

    private fun setNavigationHeader(position: Int) {
        binding.imgNavRight.visibility = View.VISIBLE
        binding.imgNavLeft.visibility = View.VISIBLE
        if (mDatePickerManager.getVisibleMonDataList().size == 1) {
            binding.imgNavLeft.visibility = View.INVISIBLE
            binding.imgNavRight.visibility = View.INVISIBLE
        } else if (position == 0) {
            binding.imgNavLeft.visibility = View.INVISIBLE
        } else if (position == mDatePickerManager.getVisibleMonDataList().size - 1) {
            binding.imgNavRight.visibility = View.INVISIBLE
        }
    }
}