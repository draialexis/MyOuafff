package fr.uca.iut.myouafff.data.persistence.converter

import androidx.room.TypeConverter
import fr.uca.iut.myouafff.data.Dog.Gender

fun Int.toGender() = enumValues<Gender>()[this]

class GenderToIntConverter {
    @TypeConverter
    fun fromInt(ordinal: Int) = ordinal.toGender()

    @TypeConverter
    fun toOrdinal(gender: Gender) = gender.ordinal
}