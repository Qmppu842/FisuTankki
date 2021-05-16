package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import fi.qmppu842.fisutankki.toScreenCoordinates

class Fish(private val body: Body, private val size: Float) {

    lateinit var img: Texture
    lateinit var sprite: Sprite

    fun render(batch: Batch) {
        batch.draw(
            sprite,
            body.position.x.toScreenCoordinates() - size / 2,
            body.position.y.toScreenCoordinates() - size / 2,
            size,
            size
        )

    }

    fun initTexture() {
        img = Texture("SpiralKoi2.png")
        sprite = Sprite(img)
    }

}