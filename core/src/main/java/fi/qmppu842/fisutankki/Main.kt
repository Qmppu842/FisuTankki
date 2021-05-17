package fi.qmppu842.fisutankki

import com.badlogic.gdx.Gdx
import com.kotcrab.vis.ui.VisUI
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug

class Main : KtxGame<KtxScreen>() {
    private var accum = 0f
    private val STEP_SIZE = 1 / 60f

    override fun create() {
        addScreen(FisuScreen())

        setScreen<FisuScreen>()

        try {
            VisUI.load()
        } catch (e: Exception) {
            debug("[Ui Skin]") { "Vis UI was probably loaded somewhere else." }

        }
    }

    override fun render() {
        accum += Gdx.graphics.deltaTime
        while (accum > STEP_SIZE) {
            accum -= STEP_SIZE
            currentScreen.render(STEP_SIZE)
        }
    }
}