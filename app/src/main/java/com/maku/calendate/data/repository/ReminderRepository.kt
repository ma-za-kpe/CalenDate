package com.maku.calendate.data.repository

import androidx.lifecycle.LiveData
import com.maku.calendate.data.db.dao.ReminderDao
import com.maku.calendate.data.db.entities.Reminder

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class ReminderRepository(private val reminderDao: ReminderDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<Reminder>> = reminderDao.getReminders()

    suspend fun insert(reminder: Reminder) {
        reminderDao.insert(reminder)
    }

    suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder)
    }
}