package fi.qmppu842.fisutankki

import com.badlogic.gdx.Gdx
import com.kotcrab.vis.ui.VisUI
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug

class Main : KtxGame<KtxScreen>() {
    override fun create() {

        addScreen(FirstScreen())

        setScreen<FirstScreen>()

        try {
            VisUI.load()
        } catch (e: Exception) {
            debug("[Ui Skin]") { "Vis UI was probably loaded somewhere else." }

        }
    }
}