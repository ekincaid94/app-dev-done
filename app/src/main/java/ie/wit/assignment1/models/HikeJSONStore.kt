package ie.wit.assignment1.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.assignment1.helpers.exists
import ie.wit.assignment1.helpers.read
import ie.wit.assignment1.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList
//import ie.wit.assignment1.models.helpers. unable to import?


const val JSON_FILE = "hikes.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<java.util.ArrayList<HikeModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HikeJSONStore(private val context: Context) : HikeStore {

    var hikes = mutableListOf<HikeModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HikeModel> {
        logAll()
        return hikes
    }

    override fun create(hike: HikeModel) {
        hike.id = generateRandomId()
        hikes.add(hike)
        serialize()
    }


    suspend override fun update(hike: HikeModel) {
        val hikesList = findAll() as ArrayList<HikeModel>
        var foundHike: HikeModel? = hikesList.find { p -> p.id == hike.id }
        if (foundHike != null) {
            foundHike.title = hike.title
            foundHike.description = hike.description
            foundHike.image = hike.image
            foundHike.location = hike.location
        }
        serialize()
    }

    override fun delete(hike: HikeModel) {
        val foundHike: HikeModel? = hikes.find { it.id == hike.id }
        hikes.remove(foundHike)
        serialize()
    }
    override fun findById(id:Long) : HikeModel? {
        val foundHike: HikeModel? = hikes.find { it.id == id }
        return foundHike
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hikes, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hikes = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        hikes.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override suspend fun clear(){
        hikes.clear()
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}