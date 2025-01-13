package com.example.testcalendar.custom_calendar

import android.view.View
import java.util.Calendar

interface DateView {
    interface OnDateClickListener {
        fun onDateClicked(view: View, selectedDate: Calendar)
    }
    enum class DateState {
        HIDDEN,
        SELECTED,
        SELECTABLE,
        DISABLE
    }

    enum class DateSelectionState {
        SELECTED,
        UNKNOWN
    }
}