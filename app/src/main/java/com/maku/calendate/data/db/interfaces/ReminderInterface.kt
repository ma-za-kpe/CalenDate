package com.maku.calendate.data.db.interfaces

import com.maku.calendate.data.db.entities.Reminder

interface ReminderInterface {
    fun getData(description: String?)
    fun delete(reminder: Reminder)
}

