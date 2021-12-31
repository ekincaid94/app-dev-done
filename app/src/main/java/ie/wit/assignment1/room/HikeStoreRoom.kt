package ie.wit.assignment1.room

import android.content.Context
import androidx.room.Room
import ie.wit.assignment1.models.HikeModel
import ie.wit.assignment1.models.HikeStore

class HikeStoreRoom(val context: Context) : HikeStore {

    var dao: HikeDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.hikeDao()
    }

    override fun findAll(): List<HikeModel> {
        return dao.findAll()
    }

    override fun findById(id: Long): HikeModel? {
        return dao.findById(id)
    }

    override fun create(hike: HikeModel) {
        dao.create(hike)
    }

    override fun update(hike: HikeModel) {
        dao.update(hike)
    }

    override fun delete(hike: HikeModel) {
        dao.deleteHike(hike)
    }

    override suspend fun clear(){
    }

    fun clear() {
    }
}