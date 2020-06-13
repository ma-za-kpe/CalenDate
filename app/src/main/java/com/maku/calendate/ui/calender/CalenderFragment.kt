package com.maku.calendate.ui.calender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.maku.calendate.R
import com.maku.calendate.databinding.FragmentCalenderBinding
import timber.log.Timber


class CalenderFragment : Fragment() {

    private lateinit var mFragmentCalenderBinding: FragmentCalenderBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        mFragmentCalenderBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_calender, container, false)

        getSelectedDate();

        return mFragmentCalenderBinding.root
    }

    /**
     *
     * get the selected date
     *
     * */
    private fun getSelectedDate() {
        val simpleCalendarView = mFragmentCalenderBinding.calender // get the reference of CalendarView

        val selectedDate = simpleCalendarView.date // get selected date in milliseconds

        Timber.d("the date is " + selectedDate)

    }

}
