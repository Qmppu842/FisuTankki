package fi.qmppu842.fisutankki

import com.badlogic.gdx.Game
import fi.qmppu842.fisutankki.FirstScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Main : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}