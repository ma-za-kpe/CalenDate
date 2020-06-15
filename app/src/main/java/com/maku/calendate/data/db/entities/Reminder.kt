package com.maku.calendate.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val REMINDER_ID = 0

@Entity(tableName = "reminder_table")

class Reminder(

    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "date") val date: Long,

    @ColumnInfo(name = "time") val time: String,

    @ColumnInfo(name = "description") val description: String

)