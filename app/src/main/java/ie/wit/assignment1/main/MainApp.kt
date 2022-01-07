package ie.wit.assignment1.main

import ie.wit.assignment1.models.HikeFireStore
import android.app.Application
import ie.wit.assignment1.models.*
import timber.log.Timber
import timber.log.Timber.i


class MainApp : Application() {

    lateinit var hikes: HikeStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        hikes = HikeFireStore(applicationContext)
        i("Hikelife is now running")
    }
}


