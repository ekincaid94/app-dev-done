package ie.wit.assignment1.views.hikelist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import ie.wit.assignment1.views.maps.HikeMapView
import ie.wit.assignment1.main.MainApp
import ie.wit.assignment1.models.HikeModel
import ie.wit.assignment1.views.hike.HikeView
import ie.wit.assignment1.views.login.LoginView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HikeListPresenter(private val view: HikeListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

    suspend fun getHikes() = app.hikes.findAll()

    fun doAddHike() {
        val launcherIntent = Intent(view, HikeView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditHike(hike: HikeModel) {
        val launcherIntent = Intent(view, HikeView::class.java)
        launcherIntent.putExtra("hike_edit", hike)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowHikesMap() {
        val launcherIntent = Intent(view, HikeMapView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    suspend fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        app.hikes.clear()
        val launcherIntent = Intent(view, LoginView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getHikes()
                }
            }
    }
    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }
}