package com.maku.calendate.ui.fragments.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maku.calendate.R
import com.maku.calendate.utils.getTime
import timber.log.Timber
import java.util.*


class PostBottomDialogFragment : BottomSheetDialogFragment(),
    View.OnClickListener {

    private var mListener: ItemClickListener?= null
    var timee:String?= null
    var eventDate: Long = 0
    lateinit var description: String

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(
        view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        //get description
        val descText = view.findViewById<EditText>(R.id.descText)
        description = descText.text.toString()

        //get date from calender
        val calenderDate = view.findViewById(R.id.calender) as CalendarView // get the reference of CalendarView
        calenderDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val c: Calendar = Calendar.getInstance()
            c.set(year, month, dayOfMonth)
            eventDate = c.getTimeInMillis()
        }

        //get time from clock
        var time = view.findViewById(R.id.alarmTimePicker) as TimePicker // get the reference of CalendarView
        time.setOnTimeChangedListener { _, hour, minute ->
            timee = getTime(hour, minute);
        }

        //send to room db and close bottom dialog
        view.findViewById<FloatingActionButton>(R.id.floating_action_button).setOnClickListener {

            Timber.d("date eventDate " + eventDate)
            Timber.d("timeeee " + timee)
            Timber.d("description " + descText.text.toString())
            
            sendToDB(eventDate, timee, descText.text.toString())

            dismiss();
        }
    }

    private fun sendToDB(eventDate: Long, timee: String?, toString: String) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is ItemClickListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                    .toString() + " must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onClick(view: View) {
        val tvSelected = view as TextView
        mListener!!.onItemClick(tvSelected.text.toString())
    }

    interface ItemClickListener {
        fun onItemClick(item: String?)
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): PostBottomDialogFragment {
            return PostBottomDialogFragment()
        }
    }
}