package com.maku.calendate.ui.fragments.list

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

import com.maku.calendate.data.db.CalendateDataBase
import com.maku.calendate.data.db.dao.ReminderDao
import com.maku.calendate.data.db.entities.Reminder
import com.maku.calendate.data.repository.ReminderRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReminderRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Reminder>>

    init {
        val remindersDao = CalendateDataBase.invoke(application).reminderDao()

        repository = ReminderRepository(remindersDao)
        allWords = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(mReminder: Reminder) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(mReminder)
    }
}