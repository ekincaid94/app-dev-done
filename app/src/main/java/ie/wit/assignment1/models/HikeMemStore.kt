package ie.wit.assignment1.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

 class HikeMemStore : HikeStore {

     val hikes = ArrayList<HikeModel>()

     override fun findAll(): List<HikeModel> {
         return hikes
     }


     override fun create(hike: HikeModel) {
         hike.id = getId()
         hikes.add(hike)
         logAll()
     }

     override fun update(hike: HikeModel) {
         val foundHike: HikeModel? = hikes.find { p -> p.id == hike.id }
         if (foundHike != null) {
             foundHike.title = hike.title
            // foundHike.location = hike.location
             foundHike.description = hike.description
             foundHike.image = hike.image
             foundHike.lat = hike.lat
             foundHike.lng = hike.lng
             foundHike.zoom = hike.zoom
             logAll()
         }
     }

     override fun findById(id:Long) : HikeModel? {
         val foundHike: HikeModel? = hikes.find { it.id == id }
         return foundHike
     }

     override fun delete(hike: HikeModel) {
         hikes.remove(hike)
     }

     private fun logAll() {
         hikes.forEach { i("$it") }
     }
 }
