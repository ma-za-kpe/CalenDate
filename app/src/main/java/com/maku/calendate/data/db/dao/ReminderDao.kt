package com.maku.calendate.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.maku.calendate.data.db.entities.Reminder


@Dao
interface ReminderDao {
    @Query("SELECT * from reminder_table")
    fun getReminders(): LiveData<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reminder: Reminder)

    @Query("DELETE FROM reminder_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(reminder: Reminder)

}