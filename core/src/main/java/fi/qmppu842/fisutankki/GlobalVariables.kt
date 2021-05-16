package fi.qmppu842.fisutankki

import com.badlogic.gdx.Gdx

object GlobalVariables {

    val sWidth = Gdx.graphics.width.toFloat()
    val sHeight = Gdx.graphics.height.toFloat()

    const val PPM = 100f
}
fun Number.toB2DCoordinates(): Float = (this.toFloat() /GlobalVariables.PPM)
fun Number.toScreenCoordinates(): Float = (this.toFloat() *GlobalVariables.PPM)