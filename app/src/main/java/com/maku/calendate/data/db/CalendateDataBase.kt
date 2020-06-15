package com.maku.calendate.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maku.calendate.data.db.dao.ReminderDao
import com.maku.calendate.data.db.entities.Reminder

@Database(
    entities = [Reminder::class],
    version = 2
)
abstract class CalendateDataBase: RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile private var instance: CalendateDataBase? = null //all threads will have immediate access to this property - volatile
        private val LOCK = Any() //dummy object to make sure no two threads are doing the same thing

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                CalendateDataBase::class.java, "CalendateDataBase.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
