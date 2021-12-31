package ie.wit.assignment1.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ie.wit.assignment1.models.HikeModel
import androidx.room.TypeConverters
import ie.wit.assignment1.helpers.Converters



@Database(entities = arrayOf(HikeModel::class), version = 1,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun hikeDao(): HikeDao
}