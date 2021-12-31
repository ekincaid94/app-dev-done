package ie.wit.assignment1.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

  class HikeMemStore : HikeStore {

    val hikes = ArrayList<HikeModel>()

    override suspend fun findAll(): List<HikeModel> {
        return hikes
    }

    override suspend fun create(hike: HikeModel) {
        hike.id = getId()
        hikes.add(hike)
        logAll()
    }

    suspend override fun update(hike: HikeModel) {
        var foundHike: HikeModel? = hikes.find { p -> p.id == hike.id }
        if (foundHike != null) {
            foundHike.title = hike.title
            foundHike.description = hike.description
            foundHike.image = hike.image
            foundHike.location = hike.location
            logAll();
        }
    }

    override suspend fun delete(hike: HikeModel) {
        hikes.remove(hike)
        logAll()
    }

    private fun logAll() {
        hikes.forEach { i("$it") }
    }
      override suspend fun clear(){
          hikes.clear()
      }


    override suspend fun findById(id: Long): HikeModel? {
        val foundHike: HikeModel? = hikes.find { it.id == id }
        return foundHike
    }
}