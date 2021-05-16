package fi.qmppu842.fisutankki

import android.os.Bundle
import android.view.WindowManager
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration = AndroidApplicationConfiguration()

        //This makes android use some kinda super fullscreen that also hides virtual buttons.
        window.decorView.systemUiVisibility = 5894
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        initialize(Main(), configuration)
    }
}