package ie.wit.assignment1.views.hike


import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.assignment1.R
import ie.wit.assignment1.databinding.ActivityHikeBinding
import ie.wit.assignment1.models.HikeModel
import com.google.android.gms.maps.GoogleMap
import timber.log.Timber.i

class HikeView : AppCompatActivity() {

    private lateinit var binding: ActivityHikeBinding
    private lateinit var presenter: HikePresenter
    lateinit var map: GoogleMap
    var hike = HikeModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHikeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = HikePresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheHike(binding.hikeTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.hikeLocation.setOnClickListener {
            presenter.cacheHike(binding.hikeTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }

        binding.mapView2.onCreate(savedInstanceState);
        binding.mapView2.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hike, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.hikeTitle.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_hike_title, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    presenter.doAddOrSave(binding.hikeTitle.text.toString(), binding.description.text.toString())
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun showHike(hike: HikeModel) {
        binding.hikeTitle.setText(hike.title)
        binding.description.setText(hike.description)

        Picasso.get()
            .load(hike.image)
            .into(binding.hikeImage)
        if (hike.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_hike_image)
        }

    }

    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.hikeImage)
        binding.chooseImage.setText(R.string.change_hike_image)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView2.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView2.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView2.onSaveInstanceState(outState)
    }

}