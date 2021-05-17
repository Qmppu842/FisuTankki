package fi.qmppu842.fisutankki

import com.badlogic.gdx.Gdx
import kotlin.random.Random

object GlobalVariables {

    //Virtual dimensions of screen
    val sWidth = Gdx.graphics.width.toFloat()
    val sHeight = Gdx.graphics.height.toFloat()

    /**
     * Pixel per meter
     * This is to scale everything Box2d does down to speed them up massively.
     */
    const val PPM = 100f

    const val outsideBorderSize = 50f

    val rand = Random(123892)

    var dtMultiplier = 1f
}

fun Number.toB2DCoordinates(): Float = (this.toFloat() / GlobalVariables.PPM)
fun Number.toScreenCoordinates(): Float = (this.toFloat() * GlobalVariables.PPM)