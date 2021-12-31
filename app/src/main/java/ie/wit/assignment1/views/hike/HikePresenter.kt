package ie.wit.assignment1.views.hike

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import ie.wit.assignment1.R
import ie.wit.assignment1.databinding.ActivityHikeBinding
import ie.wit.assignment1.main.MainApp
import ie.wit.assignment1.models.Location
import ie.wit.assignment1.models.HikeModel
import ie.wit.assignment1.helpers.showImagePicker
import ie.wit.assignment1.views.editlocation.EditLocationView
import timber.log.Timber
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ie.wit.assignment1.helpers.checkLocationPermissions
import android.annotation.SuppressLint
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import ie.wit.assignment1.helpers.createDefaultLocationRequest
import timber.log.Timber.i

class HikePresenter(private val view: HikeView) {
    private val locationRequest = createDefaultLocationRequest()
    var map: GoogleMap? = null
    var hike = HikeModel()
    var app: MainApp = view.application as MainApp
    //location service
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)

    init {

        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("hike_edit")) {
            edit = true
            hike = view.intent.extras?.getParcelable("hike_edit")!!
            view.showHike(hike)
        }
        else {

            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            hike.location.lat = location.lat
            hike.location.lng = location.lng
        }

    }


    suspend fun doAddOrSave(title: String, description: String) {
        hike.title = title
        hike.description = description
        if (edit) {
            app.hikes.update(hike)
        } else {
            app.hikes.create(hike)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    suspend fun doDelete() {
        app.hikes.delete(hike)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {

        if (hike.location.zoom != 0f) {

            location.lat =  hike.location.lat
            location.lng = hike.location.lng
            location.zoom = hike.location.zoom
            locationUpdate(hike.location.lat, hike.location.lng)
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {

        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hike.location.lat, hike.location.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        hike.location = location
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hike.title).position(LatLng(hike.location.lat, hike.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hike.location.lat, hike.location.lng), hike.location.zoom))
        view.showHike(hike)
    }

    fun cacheHike (title: String, description: String) {
        hike.title = title;
        hike.description = description
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            hike.image = result.data!!.data!!.toString()
                            view.updateImage(hike.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            hike.location = location
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun doPermissionLauncher() {
        i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}