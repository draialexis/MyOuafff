package fr.uca.iut.myouafff.data.persistence

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.uca.iut.myouafff.DogApplication
import fr.uca.iut.myouafff.data.Dog
import fr.uca.iut.myouafff.data.persistence.converter.DateToLongConverter
import fr.uca.iut.myouafff.data.persistence.converter.GenderToIntConverter
import java.util.Date


private const val DOG_DB_FILENAME = "dogs.db"

@Database(entities = [Dog::class], version = 1)
@TypeConverters(GenderToIntConverter::class, DateToLongConverter::class)
abstract class DogDatabase : RoomDatabase() {

    abstract fun dogDAO(): DogDao

    companion object {
        private lateinit var application: Application

        @Volatile
        private var instance: DogDatabase? = null


        fun getInstance(): DogDatabase {
            if (::application.isInitialized) {
                if (instance == null)
                    synchronized(this) {
                        if (instance == null) {
                            instance = Room.databaseBuilder(
                                application.applicationContext,
                                DogDatabase::class.java,
                                DOG_DB_FILENAME
                            )
                                .allowMainThreadQueries()
                                .build()

                            instance?.dogDAO()?.let {
                                if (it.getAll().isEmpty()) emptyDatabaseStub(it)
                            }
                        }
                    }
                return instance!!
            } else
                throw RuntimeException("the database must be initialized first")
        }

        @Synchronized
        fun initialize(app: DogApplication) {
            if (::application.isInitialized)
                throw RuntimeException("the same database cannot be initialize twice")

            application = app
        }

        private fun emptyDatabaseStub(dogDAO: DogDao) = with(dogDAO) {
            insert(Dog("Lassie", "Collet", Dog.Gender.FEMALE, 22.5f, 0))
            insert(Dog("Snoopy", "Beagle", Dog.Gender.MALE, 6f, 2, "Charlie Brown"))
            insert(Dog("Robert", "Caniche", Dog.Gender.MALE, 5f, 1))
            insert(Dog("Titan", "Dogue", Dog.Gender.MALE, 32f, 3, "John Doe", Date(22), 4))
        }
    }
}