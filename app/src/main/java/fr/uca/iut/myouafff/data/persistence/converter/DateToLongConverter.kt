package fr.uca.iut.myouafff.data.persistence.converter

import androidx.room.TypeConverter
import java.util.*

class DateToLongConverter {
    @TypeConverter
    fun fromTimestamp(timestamp: Long?) = timestamp?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(date: Date?) = date?.time
}