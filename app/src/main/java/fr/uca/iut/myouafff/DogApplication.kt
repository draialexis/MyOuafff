package fr.uca.iut.myouafff

import android.app.Application
import fr.uca.iut.myouafff.data.persistence.DogDatabase

class DogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DogDatabase.initialize(this)
    }
}