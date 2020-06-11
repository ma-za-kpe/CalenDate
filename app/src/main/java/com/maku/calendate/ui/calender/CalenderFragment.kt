package com.maku.calendate.ui.calender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import com.maku.calendate.R


class CalenderFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_calender, container, false)

        getSelectedDate();

        return root
    }

    private fun getSelectedDate() {
//        val simpleCalendarView = findViewById(R.id.simpleCalendarView) as CalendarView // get the reference of CalendarView

//        val selectedDate =
//            simpleCalendarView.date // get selected date in milliseconds

    }

}
