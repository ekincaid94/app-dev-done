package ie.wit.assignment1.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HikeFireStore(val context: Context) : HikeStore {
    val hikes = ArrayList<HikeModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<HikeModel> {
        return hikes
    }

    override suspend fun findById(id: Long): HikeModel? {
        val foundHike: HikeModel? = hikes.find { p -> p.id == id }
        return foundHike
    }

    override suspend fun create(hike: HikeModel) {
        val key = db.child("users").child(userId).child("hikes").push().key
        key?.let {
            hike.fbId = key
            hikes.add(hike)
            db.child("users").child(userId).child("hikes").child(key).setValue(hike)
        }
    }

    override suspend fun update(hike: HikeModel) {
        var foundHike: HikeModel? = hikes.find { p -> p.fbId == hike.fbId }
        if (foundHike != null) {
            foundHike.title = hike.title
            foundHike.description = hike.description
            foundHike.image = hike.image
            foundHike.location = hike.location
        }

        db.child("users").child(userId).child("hikes").child(hike.fbId).setValue(hike)

    }

    override suspend fun delete(hike: HikeModel) {
        db.child("users").child(userId).child("hikes").child(hike.fbId).removeValue()
        hikes.remove(hike)
    }

    override suspend fun clear() {
        hikes.clear()
    }

    fun fetchHikes(hikesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(hikes) {
                    it.getValue<HikeModel>(
                        HikeModel::class.java
                    )
                }
                hikesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance("https://hike-e24c4-default-rtdb.europe-west1.firebasedatabase.app").reference
        hikes.clear()
        db.child("users").child(userId).child("hikes")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}