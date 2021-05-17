package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import fi.qmppu842.fisutankki.GlobalVariables
import fi.qmppu842.fisutankki.toB2DCoordinates
import fi.qmppu842.fisutankki.toScreenCoordinates
import ktx.log.info

class Fish(private val body: Body, private val size: Float) {

    lateinit var img: Texture
    lateinit var sprite: Sprite
    private val gVars = GlobalVariables

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
        donutfyTheWorld()
    }

    /**
     * When Fish tries to exit too far, this method will bring them back on the other side of the world.
     * Well, its proper name would be screen wrap x and y wise.
     */
    fun donutfyTheWorld() {
        screenWrapX()
        screenWrapY()
    }

    private fun screenWrapY() {
        var fishY = body.position.y
        if ((0 - gVars.outsideBorderSize).toB2DCoordinates() > fishY) {
            fishY = (gVars.sHeight + gVars.outsideBorderSize / 2).toB2DCoordinates()
        } else if ((gVars.sHeight + gVars.outsideBorderSize).toB2DCoordinates() < fishY) {
            fishY = (0 - gVars.outsideBorderSize / 2).toB2DCoordinates()
        }
        body.setTransform(body.position.x, fishY, body.angle)
    }

    private fun screenWrapX() {
        var fishX = body.position.x
        if ((0 - gVars.outsideBorderSize).toB2DCoordinates() > fishX) {
            fishX = (gVars.sWidth + gVars.outsideBorderSize / 2).toB2DCoordinates()
        } else if ((gVars.sWidth + gVars.outsideBorderSize).toB2DCoordinates() < fishX) {
            fishX = (0 - gVars.outsideBorderSize / 2).toB2DCoordinates()
        }
        body.setTransform(fishX, body.position.y, body.angle)
    }

}