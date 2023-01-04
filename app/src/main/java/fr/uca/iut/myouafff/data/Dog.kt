package fr.uca.iut.myouafff.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val NEW_DOG_ID = 1337L

@Entity(tableName = "dogs")
class Dog(
    var name: String = "",
    var breed: String = "",
    var gender: Gender = Gender.UNKNOWN,
    var weight: Float = 0f,
    var aggressiveness: Int = 0,
    var owner: String? = null,
    var admissionDate: Date? = null,
    @PrimaryKey(autoGenerate = true) val id: Long = NEW_DOG_ID
) {
    enum class Gender {
        UNKNOWN,
        MALE,
        FEMALE
    }
}