package ie.wit.assignment1.activities

//import android.location.Location
//import timber.log.Timber
import android.content.Intent
//import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.assignment1.R
import ie.wit.assignment1.databinding.ActivityHikeBinding
import ie.wit.assignment1.helpers.showImagePicker
import ie.wit.assignment1.main.MainApp
import ie.wit.assignment1.models.HikeModel
import ie.wit.assignment1.models.Location
import timber.log.Timber.i
import android.widget.Toast


class HikeActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var binding: ActivityHikeBinding

    //var location = Location(52.245696, -7.139102, 15f)
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    var hike = HikeModel()
    lateinit var app: MainApp
    var edit =false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHikeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        registerImagePickerCallback()
        registerMapCallback()

        i("Hike life Activity started...")

        if (intent.hasExtra("hike_edit")) {
            edit = true
            hike = intent.extras?.getParcelable("hike_edit")!!
            binding.hikeTitle.setText(hike.title)
            binding.description.setText(hike.description)
            binding.btnAdd.setText(R.string.save_hike)
            Picasso.get()
                .load(hike.image)
                .into(binding.hikeImage)
            if (hike.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_hike_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            hike.title = binding.hikeTitle.text.toString()
            //hike.location = binding.hikeLocation.text.toString()
            hike.description = binding.description.text.toString()
            if (hike.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_hike_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.hikes.update(hike.copy())
                } else {
                    app.hikes.create(hike.copy())
                }
            }
            i("add Button Pressed: $hike")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.hikeLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (hike.zoom != 0f) {
                location.lat =  hike.lat
                location.lng = hike.lng
                location.zoom = hike.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
    }

    binding.chooseImage.setOnClickListener {
        showImagePicker(imageIntentLauncher)
        i("Select image")
    }

}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hike, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                app.hikes.delete(hike)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            hike.image = result.data!!.data!!
                            Picasso.get()
                                .load(hike.image)
                                .into(binding.hikeImage)
                            binding.chooseImage.setText(R.string.change_hike_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            hike.lat = location.lat
                            hike.lng = location.lng
                            hike.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}