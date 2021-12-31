package ie.wit.assignment1.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.assignment1.databinding.ActivityHikeMapsBinding
import ie.wit.assignment1.databinding.ContentHikeMapsBinding
import ie.wit.assignment1.main.MainApp
import ie.wit.assignment1.R


 class HikeMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {


    private lateinit var binding: ActivityHikeMapsBinding
    private lateinit var contentBinding: ContentHikeMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_hike_maps)
       app = application as MainApp
        binding = ActivityHikeMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        contentBinding = ContentHikeMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }


    fun configureMap() {
        map.setOnMarkerClickListener(this)
        map.uiSettings.setZoomControlsEnabled(true)
        app.hikes.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

     override fun onMarkerClick(marker: Marker): Boolean {
         val tag = marker.tag as Long
         val hike = app.hikes.findById(tag)
         currentTitle.text = hike!!.title
         currentDescription.text = hike!!.description
         imageView.setImageBitmap(readImageFromPath(this@HikeMapsActivity, hike.image))
         return true
     }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
}

