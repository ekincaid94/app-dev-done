package ie.wit.assignment1.room

import androidx.room.*
import ie.wit.assignment1.models.HikeModel


@Dao
interface HikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hike: HikeModel)

    @Query("SELECT * FROM HikeModel")
    fun findAll(): List<HikeModel>

    @Query("select * from HikeModel where id = :id")
    fun findById(id: Long): HikeModel

    @Update
    fun update(hike: HikeModel)

    @Delete
    fun deleteHike(hike: HikeModel)
}