package ie.wit.assignment1.activities

//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import ie.wit.assignment1.databinding.CardHikeBinding
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.assignment1.R
import ie.wit.assignment1.adapters.HikeAdapter
import ie.wit.assignment1.adapters.HikeListener
import ie.wit.assignment1.databinding.ActivityHikeListBinding
import ie.wit.assignment1.main.MainApp
import ie.wit.assignment1.models.HikeModel

 class HikeListActivity : AppCompatActivity(), HikeListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityHikeListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
     private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHikeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadHikes()

        registerRefreshCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
             R.id.item_add -> {
                 val launcherIntent = Intent(this, HikeActivity::class.java)
                 refreshIntentLauncher.launch(launcherIntent)
             }
             R.id.item_map -> {
                 val launcherIntent = Intent(this, HikeMapsActivity::class.java)
                 mapIntentLauncher.launch(launcherIntent)
             }
         }
         return super.onOptionsItemSelected(item)
     }

    override fun onHikeClick(hike: HikeModel) {
        val launcherIntent = Intent(this, HikeActivity::class.java)
        launcherIntent.putExtra("hike_edit", hike)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadHikes() }
    }

     private fun registerMapCallback() {
         mapIntentLauncher =
             registerForActivityResult(ActivityResultContracts.StartActivityForResult())
             {  }
     }

   private fun loadHikes() {
    showHikes(app.hikes.findAll())
     }

    fun showHikes (hikes: List<HikeModel>) {
    binding.recyclerView.adapter = HikeAdapter(hikes, this)
    binding.recyclerView.adapter?.notifyDataSetChanged()
}


    }
