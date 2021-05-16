package fi.qmppu842.fisutankki

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.utils.ScreenUtils
import fi.qmppu842.fisutankki.simulation_bits.WorldHolder
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.log.info

/** First screen of the application. Displayed after the application is created.  */
class FisuScreen : KtxScreen {

    private val Box2DDebugRenderer: Box2DDebugRenderer = Box2DDebugRenderer()
    /**
     * Camera used to scale Box2d world back to normal size for debug rendering.
     */
    private val b2dCam: OrthographicCamera = OrthographicCamera()

    private val cam = OrthographicCamera()
    private val batch: SpriteBatch

    private val gVars = GlobalVariables

    init {
        batch = SpriteBatch()
        b2dCam.setToOrtho(false, gVars.sWidth.toB2DCoordinates(), gVars.sHeight.toB2DCoordinates())
        cam.setToOrtho(false, gVars.sWidth, gVars.sHeight)
    }


    override fun show() {
        // Prepare your screen here.
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(1f, 0f, 0f, 1f)

        //Step simulation one step forward
        WorldHolder.worldHolder.update(delta)


        //Actual drawing of all the things
        batch.use(cam) {
            WorldHolder.worldHolder.render(it)
        }

        //Debug rendering
        Box2DDebugRenderer.render(WorldHolder.worldHolder.world, b2dCam.combined)

    }

    override fun resize(width: Int, height: Int) {
        // Resize your screen here. The parameters represent the new window size.
    }

    override fun pause() {
        // Invoked when your application is paused.
    }

    override fun resume() {
        // Invoked when your application is resumed after pause.
    }

    override fun hide() {
        // This method is called when another screen replaces this one.
    }

    override fun dispose() {
        // Destroy screen's assets here.
    }
}