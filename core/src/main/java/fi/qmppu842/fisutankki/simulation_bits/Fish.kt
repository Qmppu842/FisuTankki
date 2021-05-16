package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import fi.qmppu842.fisutankki.toScreenCoordinates

class Fish(private val body: Body, private val size: Float) {

    lateinit var img: Texture
    lateinit var sprite: Sprite

    fun initTexture() {
        img = Texture("SpiralKoi2.png")
        sprite = Sprite(img)
    }

    fun render(batch: Batch) {
        batch.draw(
            sprite,
            body.position.x.toScreenCoordinates() - size / 2,
            body.position.y.toScreenCoordinates() - size / 2,
            size,
            size
        )

    }

    fun update(dt: Float) {
//        body.linearVelocity = Vector2(1f, 1f)
        body.setLinearVelocity(1f, 1f)
    }

    /**
     * When Fish tries to exit too far, this method will bring them back on the other side of the world.
     * Well, its proper name would be screen wrap x and y wise.
     */
    fun donutfyTheWorld() {
        screenWrapX()
        screenWrapY()
    }

    fun screenWrapY() {

    }

    fun screenWrapX() {
        var fishX = body.position.x
    }

}