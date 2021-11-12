package ie.wit.assignment1.main

import android.app.Application
import ie.wit.assignment1.models.HikeJSONStore
import ie.wit.assignment1.models.HikeStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    //val hikes = ArrayList<HikeModel>()
   lateinit var hikes : HikeStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        hikes = HikeJSONStore(applicationContext)
        i("HikeLife started")
        //hikes.add(HikeModel("Hike One", "About one..."))
        //hikes.add(HikeModel("Hike Two", "About two..."))
        //hikes.add(HikeModel("Hike Three", "About three..."))
    }
}


