package com.maku.calendate.ui.fragments.list

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maku.calendate.R
import com.maku.calendate.data.adapters.RemindersAdapters
import com.maku.calendate.data.db.entities.Reminder
import com.maku.calendate.data.db.interfaces.ReminderInterface
import com.maku.calendate.databinding.ListFragmentBinding
import com.shreyaspatil.MaterialDialog.MaterialDialog
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ListFragment : Fragment(), PostBottomDialogFragment.ItemClickListener {

    private lateinit var mFragmentListBinding: ListFragmentBinding

    private var callback: ReminderInterface? = null

    private lateinit var mListViewModel: ListViewModel

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mFragmentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.list_fragment, container, false)

        val adapter = RemindersAdapters(requireContext(), {item ->
            getDetailsForAlarm(item)
        }, {item ->
            removeItem(item)
        }, {item ->
            onClickToShowItemDetails(item)
        })
        mFragmentListBinding.recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        mFragmentListBinding.recyclerView.layoutManager = layoutManager

        mListViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        mListViewModel.allWords.observe(requireActivity(), Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setWords(it) }
        })

        mFragmentListBinding.button.setOnClickListener {
            showBottomSheet()
        }

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )

        return mFragmentListBinding.root
    }

    private fun onClickToShowItemDetails(item: Any) {

        val descriptionFromDB = item as Reminder
        val dateFromDB: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(item.date)


        MaterialDialog.Builder(requireActivity())
            .setTitle("Reminder")
            .setMessage(descriptionFromDB.description + "\n" + item.time + "\n" + dateFromDB + "\n")
            .setPositiveButton("View") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setNegativeButton("Exit") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .build()
            .show()

    }

    private fun createChannel(notification_channel_id: String, notification_channel_name: String) {

        // create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notification_channel_id,
                notification_channel_name,
                // change importance
                NotificationManager.IMPORTANCE_HIGH
            )// disable badges for this channel
                .apply {
                    setShowBadge(true)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
//            notificationChannel.description = getString(R.string.breakfast_notification_channel_description)

            //Get an instance of NotificationManager by calling getSystemService().
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }

    private fun getDetailsForAlarm(item: Any) {
        Timber.d("get detials for alarm " + item as Reminder)

        val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

        // get current time and date
        Timber.d(" currentDate " + currentDate)
        Timber.d(" currentTime " + currentTime)

        val timeFromDB = item as Reminder
        val dateFromDB: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(timeFromDB.date)

        //get time from DB
        Timber.d(" DB time " + timeFromDB.time)
        Timber.d(" DB date " + dateFromDB)

        //compare the two times
        if (timeFromDB.time == currentTime && dateFromDB == currentDate) {
            Toast.makeText(requireContext(), "times are equal", Toast.LENGTH_LONG).show()

            //construct the message
            val message = item.description + " at " + timeFromDB.time + " on " + dateFromDB

            // create a notification
//            val notificationManager =
//                ContextCompat.getSystemService(
//                    requireContext(),
//                    NotificationManager::class.java
//                ) as NotificationManager
//            notificationManager.sendNotification(message, requireContext())

        } else if (timeFromDB.time >= currentTime && dateFromDB >= currentDate) {

            Toast.makeText(requireContext(), "time from db is greater than current time", Toast.LENGTH_LONG).show()

            // cancel the notification
//            val notificationManager =
//                ContextCompat.getSystemService(
//                    requireContext(),
//                    NotificationManager::class.java
//                ) as NotificationManager
//            notificationManager.cancelNotifications()

        } else if (timeFromDB.time <= currentTime && dateFromDB <= currentDate) {

            Toast.makeText(requireContext(), "time from db is lower than current time", Toast.LENGTH_LONG).show()

            // cancel the notification
//            val notificationManager =
//                ContextCompat.getSystemService(
//                    requireContext(),
//                    NotificationManager::class.java
//                ) as NotificationManager
//            notificationManager.cancelNotifications()

        }

    }

    private fun removeItem(item: Any) {
        mListViewModel.delete(item as Reminder)
    }


    fun showBottomSheet() {
        val addPhotoBottomDialogFragment: PostBottomDialogFragment = PostBottomDialogFragment.newInstance(callback)
        activity?.getSupportFragmentManager()?.let {
            addPhotoBottomDialogFragment.show(
                it,
                PostBottomDialogFragment.TAG
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onItemClick(item: String?) {
        Timber.d("desc " + item)
    }

}