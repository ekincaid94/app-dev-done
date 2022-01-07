package ie.wit.assignment1.models


import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ie.wit.assignment1.helpers.readImageFromPath
import timber.log.Timber.i
import java.io.ByteArrayOutputStream
import java.io.File

class HikeFireStore(val context: Context) : HikeStore {
    val hikes = ArrayList<HikeModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
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
            updateImage(hike)
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
        if(hike.image.length > 0){
            updateImage(hike)
        }
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
        st = FirebaseStorage.getInstance().reference
        db = FirebaseDatabase.getInstance("https://hike-e24c4-default-rtdb.europe-west1.firebasedatabase.app").reference
        hikes.clear()
        db.child("users").child(userId).child("hikes")
            .addListenerForSingleValueEvent(valueEventListener)
    }
    fun updateImage(hike: HikeModel){
        if(hike.image != ""){
            val fileName = File(hike.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, hike.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)

                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        hike.image = it.toString()
                        db.child("users").child(userId).child("hikes").child(hike.fbId).setValue(hike)
                    }
                }.addOnFailureListener{
                    var errorMessage = it.message
                    i("Failure: $errorMessage")
                }
            }

        }
    }
}