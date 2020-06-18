package com.maku.calendate.ui.fragments.list

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maku.calendate.R
import com.maku.calendate.data.db.entities.Reminder
import com.maku.calendate.data.db.interfaces.ReminderInterface
import com.maku.calendate.utils.getTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class PostBottomDialogFragment : BottomSheetDialogFragment(),
    View.OnClickListener {

    private var mListener: ItemClickListener?= null
    lateinit var timee:String
    var eventDate: Long = 0
    var zoroDate: Long = 0
    lateinit var description: String
    private lateinit var mListViewModel: ListViewModel

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        mListViewModel = ViewModelProvider(this).get(ListViewModel::class.java)

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
            timee = getTime(hour, minute).toString();
        }

        //send to room db and close bottom dialog
        view.findViewById<FloatingActionButton>(R.id.floating_action_button).setOnClickListener {
            val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            if (eventDate.equals(zoroDate)){
                val c = Calendar.getInstance().time
                //                val l: Long = currentDate.toLong()

                eventDate = c.time
                Timber.d("date eventDate not zero " + eventDate)
            }

            Timber.d("date eventDate " + eventDate)
            Timber.d("timeeee " + timee)
            Timber.d("description " + descText.text.toString())

            sendToDB(eventDate, timee, descText.text.toString())

            dismiss();
        }
    }

    private fun sendToDB(eventDate: Long, timee: String, desc: String) {

        if (TextUtils.isEmpty(desc)) {
            Toast.makeText(
                requireContext(),
                "enter something",
                Toast.LENGTH_LONG).show()
        } else {
            val randomDouble = Math.random()
            val word = Reminder(randomDouble.toInt(), eventDate, timee, desc)
            mListViewModel.insert(word)
        }
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
        mListener!!.onItemClick(description)
    }

    interface ItemClickListener {
        fun onItemClick(item: String?) {}
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(callback: ReminderInterface?): PostBottomDialogFragment {
//            callback = callback
            return PostBottomDialogFragment()
        }
    }

}