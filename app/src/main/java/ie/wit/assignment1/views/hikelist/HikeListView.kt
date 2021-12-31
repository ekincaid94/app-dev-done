package ie.wit.assignment1.views.hikelist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.assignment1.R
import ie.wit.assignment1.views.hikelist.HikeAdapter
import ie.wit.assignment1.views.hikelist.HikeListener
import ie.wit.assignment1.databinding.ActivityHikeListBinding
import ie.wit.assignment1.main.MainApp
import ie.wit.assignment1.models.HikeModel

class HikeListView : AppCompatActivity(), HikeListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityHikeListBinding
    lateinit var presenter: HikeListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHikeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = HikeListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadHikes()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddHike() }
            R.id.item_map -> { presenter.doShowHikesMap() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHikeClick(hike: HikeModel) {
        presenter.doEditHike(hike)

    }

    private fun loadHikes() {
        binding.recyclerView.adapter = HikeAdapter(presenter.getHikes(), this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}