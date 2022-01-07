package ie.wit.assignment1.models

interface HikeStore {
    suspend fun findAll(): List<HikeModel>
    suspend fun create(hike: HikeModel)
    suspend fun update(hike: HikeModel)
    suspend fun delete(hike: HikeModel)
    suspend fun findById(id:Long) : HikeModel?
    suspend fun clear()
}
