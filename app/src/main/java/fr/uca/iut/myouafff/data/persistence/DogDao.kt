package fr.uca.iut.myouafff.data.persistence

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import fr.uca.iut.myouafff.data.Dog

@Dao
interface DogDao {

    @Query("SELECT * FROM dogs")
    fun getAll(): List<Dog>

    @Query("SELECT * FROM dogs WHERE id = :id")
    fun findById(id: Long): Dog

    @Insert(onConflict = REPLACE)
    fun insert(dog: Dog)

    @Insert
    fun insertAll(vararg dogs: Dog)

    @Update(onConflict = REPLACE)
    fun update(dog: Dog)

    @Delete
    fun delete(dog: Dog)
}