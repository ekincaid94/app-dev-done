package ie.wit.assignment1.views.login

import ie.wit.assignment1.models.HikeFireStore
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import ie.wit.assignment1.main.MainApp
import ie.wit.assignment1.views.hikelist.HikeListView


class LoginPresenter (val view: LoginView)  {
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>
    var app: MainApp = view.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: HikeFireStore? = null

    init{
        registerLoginCallback()
        if (app.hikes is HikeFireStore) {
            fireStore = app.hikes as HikeFireStore
        }
    }


    fun doLogin(email: String, password: String) {
        view.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchHikes {
                        view?.hideProgress()
                        val launcherIntent = Intent(view, HikeListView::class.java)
                        loginIntentLauncher.launch(launcherIntent)
                    }
                } else {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, HikeListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            } else {
                view?.hideProgress()
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }

    }

    fun doSignUp(email: String, password: String) {
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                fireStore!!.fetchHikes {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, HikeListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            } else {
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }
    }
    
    private fun registerLoginCallback(){
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}