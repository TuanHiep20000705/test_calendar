package com.example.testcalendar.custom_calendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.testcalendar.R
import com.example.testcalendar.databinding.LayoutCustomDateViewBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CustomDateView(
    context: Context,
    attrs: AttributeSet?,
) : ConstraintLayout(context, attrs) {
    private var binding: LayoutCustomDateViewBinding
    private var onDateClickListener: DateView.OnDateClickListener? = null
    private var mDateState: DateView.DateState
    private val mViewClickListener = OnClickListener {
        val key = it.tag as Long
        if (onDateClickListener != null) {
            val selectedCal = Calendar.getInstance()
            var date = Date()
            try {
                date = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).parse(key.toString())!!
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            selectedCal.time = date
            onDateClickListener?.onDateClicked(it, selectedCal)
        }
    }
    private var disableDateColor = ContextCompat.getColor(context, R.color.disable_date_color)
    private var defaultDateColor = ContextCompat.getColor(context, R.color.default_date_color)

    init {
        binding = LayoutCustomDateViewBinding.inflate(LayoutInflater.from(context), this, true)
        mDateState = DateView.DateState.SELECTABLE
        updateDateBackground(mDateState)
    }

    fun setDateText(date: String) {
        binding.dayOfMonthText.text = date
    }

    fun setDateClickListener(listener: DateView.OnDateClickListener) {
        onDateClickListener = listener
    }

    fun updateDateBackground(dateState: DateView.DateState) {
        mDateState = dateState
        when (dateState) {
            DateView.DateState.HIDDEN -> {
                binding.dayOfMonthText.text = ""
                binding.dayOfMonthText.setBackgroundColor(Color.TRANSPARENT)
                binding.viewStrip.setBackgroundColor(Color.TRANSPARENT)
                setBackgroundColor(Color.TRANSPARENT)
                visibility = View.INVISIBLE
                setOnClickListener(null)
            }
            DateView.DateState.DISABLE -> {
                binding.dayOfMonthText.setBackgroundColor(Color.TRANSPARENT)
                binding.viewStrip.setBackgroundColor(Color.TRANSPARENT)
                setBackgroundColor(Color.TRANSPARENT)
                binding.dayOfMonthText.setTextColor(disableDateColor)
                visibility = View.VISIBLE
                setOnClickListener(null)
            }
            DateView.DateState.SELECTABLE -> {
                binding.dayOfMonthText.setBackgroundColor(Color.TRANSPARENT)
                binding.viewStrip.setBackgroundColor(Color.TRANSPARENT)
                setBackgroundColor(Color.TRANSPARENT)
                binding.dayOfMonthText.setTextColor(defaultDateColor)
                visibility = View.VISIBLE
                setOnClickListener(mViewClickListener)
            }
            DateView.DateState.SELECTED -> {
                Log.e(">>>>", "aaa")
                val mDrawableTvDate = ContextCompat.getDrawable(context, R.drawable.bg_circle_selected_date)
                binding.dayOfMonthText.text = "aaa"
                binding.dayOfMonthText.background = mDrawableTvDate
            }
        }
    }
}