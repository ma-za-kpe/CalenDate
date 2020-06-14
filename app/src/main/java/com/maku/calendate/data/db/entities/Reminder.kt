package com.maku.calendate.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")

class Reminder(

    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "description") val description: String

)