package ie.wit.assignment1.models

interface HikeStore {
    fun findAll(): List<HikeModel>
    fun create(hike: HikeModel)
    fun update(hike: HikeModel)
    fun delete(hike: HikeModel)
    fun findById(id:Long) : HikeModel?
    suspend fun clear()
}
