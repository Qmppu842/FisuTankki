package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import ktx.box2d.fixture
import kotlin.math.roundToInt

class Fish(val body: Body, val size: Float) {

    lateinit var img: Texture
    lateinit var sprite: Sprite

    fun render(batch: Batch) {
        batch.draw(sprite, body.position.x - size / 2, body.position.y - size / 2, size, size)

    }

    fun initTexture() {
        img = Texture("SpiralKoi2.png")
        sprite = Sprite(img)//, size.roundToInt(), size.roundToInt())
    }

}