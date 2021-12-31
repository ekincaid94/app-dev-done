package ie.wit.assignment1.views.maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import ie.wit.assignment1.databinding.ActivityHikeMapsBinding
import ie.wit.assignment1.databinding.ContentHikeMapsBinding
import ie.wit.assignment1.main.MainApp
import ie.wit.assignment1.models.HikeModel


class HikeMapView : AppCompatActivity() , GoogleMap.OnMarkerClickListener{

    private lateinit var binding: ActivityHikeMapsBinding
    private lateinit var contentBinding: ContentHikeMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: HikeMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityHikeMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = HikeMapPresenter(this)

        contentBinding = ContentHikeMapsBinding.bind(binding.root)

        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync{
            presenter.doPopulateMap(it)
        }
    }
    fun showHike(hike: HikeModel) {
        contentBinding.currentTitle.text = hike.title
        contentBinding.currentDescription.text = hike.description
        Picasso.get()
            .load(hike.image)
            .into(contentBinding.imageView2)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
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