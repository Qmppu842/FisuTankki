package fi.qmppu842.fisutankki

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import fi.qmppu842.fisutankki.simulation_bits.Fish
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

    val rand = Random(System.currentTimeMillis())

    var dtMultiplier = 1f

    val fishFilter = 2
    val attractFilter = 4
    val alignFilter = 8
    val repulseFilter = 16
    val wallFilter = 32
    val senseFilter = 64

    val wallExtra = 127

    val amountOfFishes = 30

    lateinit var img1: Texture
    lateinit var img2: Texture

    fun initTexture() {
        img1 = Texture("SpiralKoi.png")
        img2 = Texture("SpiralKoi2.png")
    }

    fun getTexture(): Texture {
        return if (rand.nextBoolean()) {
            img1
        } else {
            img2
        }
    }

    fun dispose(){
        img1.dispose()
        img2.dispose()
    }
}

fun Number.toB2DCoordinates(): Float = (this.toFloat() / GlobalVariables.PPM)
fun Number.toScreenCoordinates(): Float = (this.toFloat() * GlobalVariables.PPM)

fun nextInRange(range: IntRange): Int = GlobalVariables.rand.nextInt(range.first, range.last)
fun nextInRange(range: LongRange): Long = GlobalVariables.rand.nextLong(range.first, range.last)

//Sooo hacky way to get semi nice Float range randoms
fun nextInRange(range: ClosedFloatingPointRange<Float>): Float =
    GlobalVariables.rand.nextDouble(range.start.toDouble(), range.endInclusive.toDouble()).toFloat()