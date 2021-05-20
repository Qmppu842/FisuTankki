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

    val rand = Random(123890)

    var dtMultiplier = 1f

    val fishFilter = 2
    val attractFilter = 4
    val alignFilter = 8
    val repulseFilter = 16
    val wallFilter = 32
    val senseFilter = 64

    val wallExtra = 127
}

fun Number.toB2DCoordinates(): Float = (this.toFloat() / GlobalVariables.PPM)
fun Number.toScreenCoordinates(): Float = (this.toFloat() * GlobalVariables.PPM)

fun nextInRange(range: IntRange): Int = GlobalVariables.rand.nextInt(range.first, range.last)
fun nextInRange(range: LongRange): Long = GlobalVariables.rand.nextLong(range.first, range.last)

//Sooo hacky way to get semi nice Float range randoms
fun nextInRange(range: ClosedFloatingPointRange<Float>): Float =
    GlobalVariables.rand.nextDouble(range.start.toDouble(), range.endInclusive.toDouble()).toFloat()