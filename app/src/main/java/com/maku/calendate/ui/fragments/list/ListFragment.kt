package com.maku.calendate.ui.fragments.list

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maku.calendate.CalenDate
import com.maku.calendate.R
import com.maku.calendate.data.adapters.RemindersAdapters
import com.maku.calendate.data.db.entities.Reminder
import com.maku.calendate.data.db.interfaces.ReminderInterface
import com.maku.calendate.databinding.ListFragmentBinding
import com.maku.calendate.receiver.AlarmReceiver
import com.shreyaspatil.MaterialDialog.MaterialDialog
import timber.log.Timber
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class ListFragment : Fragment(), PostBottomDialogFragment.ItemClickListener {

    private lateinit var mFragmentListBinding: ListFragmentBinding

    private var callback: ReminderInterface? = null

    private lateinit var mListViewModel: ListViewModel

    companion object {
        fun newInstance() = ListFragment()
    }

    // Notification ID.
    private val NOTIFICATION_ID = 0

    // Notification channel ID.
    private val PRIMARY_CHANNEL_ID = R.string.notification_channel_id

    private val PRIMARY_CHANNEL_NAME = R.string.notification_channel_name

    private var mNotificationManager: NotificationManager? = null

    private lateinit var viewModel: ListViewModel
    private val REQUEST_CODE = 0
    private val TRIGGER_TIME = "TRIGGER_AT"

    private val minute: Long = 60_000L
    private val second: Long = 1_000L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mFragmentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.list_fragment, container, false)

        val adapter = RemindersAdapters(requireContext(), {item ->
            setAlarm(item)
        }, {item ->
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

        mFragmentListBinding.teast.setOnClickListener {v ->
            whatsApp(v)
        }

        mFragmentListBinding.button.setOnClickListener {
            showBottomSheet()
        }

        createChannel(
            getString(PRIMARY_CHANNEL_ID),
            getString(PRIMARY_CHANNEL_NAME)
        )

        return mFragmentListBinding.root
    }

    private fun setAlarm(item: Any) {

        Toast.makeText(requireContext(), "Alarm is up.", Toast.LENGTH_LONG).show()

        // Set up the Notification Broadcast Intent.
        val notifyIntent = Intent(requireContext(), AlarmReceiver::class.java)

        //To track the state of the alarm, you need a boolean variable that is true if the alarm exists, and false otherwise. To set this boolean, you can call PendingIntent.getBroadcast() with the FLAG_NO_CREATE flag. If a PendingIntent exists, that PendingIntent is returned; otherwise the call returns null.
        val alarmUp = PendingIntent.getBroadcast(
            requireContext(), NOTIFICATION_ID,
            notifyIntent, PendingIntent.FLAG_NO_CREATE
        ) != null

        val notifyPendingIntent = PendingIntent.getBroadcast(
            requireContext(), NOTIFICATION_ID, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager: AlarmManager = CalenDate.applicationContext().getSystemService(ALARM_SERVICE) as AlarmManager

        //current time
        val currentTime: String = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val formatter = SimpleDateFormat("hh:mm a")
        val date1 = formatter.parse(currentTime);


        //DB time
        val time = item as Reminder
        val formatter2 = SimpleDateFormat("hh:mm a")
        val date2 = formatter2.parse(item.time);
        Timber.d(" DB  " + date2 + " currentTime  " + date1)

        //get the difference of time in minutes
        val difference: Long = date2.time - date1.time
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
        val min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)

        Timber.d(" min  " + min)
        val currentTime1: String = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date().time)

        Timber.d(" SystemClock.currentThreadTimeMillis()()  " + currentTime1 )

        //check if time has passed, negative time like (-1 minutes)
        if (min < 0) {
            // it's a negative time
            Toast.makeText(requireContext(), "Time has gone.", Toast.LENGTH_LONG).show()
        } else {
            // it's a positive time
            Toast.makeText(requireContext(), " you have time left.", Toast.LENGTH_LONG).show()
            alarmManager.set(AlarmManager.RTC,
                Date().time +
                        60 * 1000 * 1, notifyPendingIntent);
        }

    }

    private fun whatsappInstalledOrNot(uri: String): Boolean {
        val pm: PackageManager = CalenDate.applicationContext().packageManager
        var app_installed = false
        app_installed = try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }

    fun whatsApp(v: View?) {
        val smsNumber = "254700432103"
        val isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp")
        if (isWhatsappInstalled) {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra(
                "jid",
                PhoneNumberUtils.stripSeparators(smsNumber).toString() + "@s.whatsapp.net"
            ) //phone number without "+" prefix
            startActivity(sendIntent)
        } else {
            val uri: Uri = Uri.parse("market://details?id=com.whatsapp")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            Toast.makeText(
                requireContext()
                , "WhatsApp not Installed",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(goToMarket)
        }
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

        val dateFromDB: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(item.date)

        //get time from DB
        Timber.d(" DB time " + item.time)
        Timber.d(" DB date " + dateFromDB)

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