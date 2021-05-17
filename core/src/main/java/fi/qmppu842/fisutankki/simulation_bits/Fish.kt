package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import fi.qmppu842.fisutankki.GlobalVariables
import fi.qmppu842.fisutankki.toB2DCoordinates
import fi.qmppu842.fisutankki.toScreenCoordinates
import ktx.box2d.body
import ktx.box2d.circle
import ktx.log.info
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Fish(private val body: Body, private val size: Float) {

     lateinit var img: Texture
     lateinit var sprite: Sprite
    private val gVars = GlobalVariables

    private var velocity = 2f


    companion object FishCurator {

        private val gVars = GlobalVariables
        private val rand = Random(123890)

        /**
         * World: The world to add the fish.
         * Radius: how big the fish should be.
         * posX: x position of the fish.
         * posY: y position of the fish.
         *
         * Returns the ready grilled fish at perfect body temperature.
         */
        fun addFishToWorld(
            world: World,
            radius: Float = 25f,
            posX: Float = gVars.sWidth.toB2DCoordinates() / 2,
            posY: Float = gVars.sHeight.toB2DCoordinates() / 2,
            angle: Float = 0f
        ): Fish {

            val body: Body = world.body {
                position.set(posX, posY)
                type = BodyDef.BodyType.DynamicBody
                this.angle = angle

                circle(radius = radius.toB2DCoordinates()) {
                    restitution = 1.5f
                    density = 1090f
                }
            }

            val fisu = Fish(body, radius * 2)
            fisu.initTexture()
            return fisu
        }

        fun addRandomFishToWorld(world: World): Fish {
            val radius = rand.nextInt(15, 31).toFloat()
            val angle = rand.nextDouble(Math.PI * 2).toFloat()
            val posX = rand.nextDouble(gVars.sWidth.toDouble()).toB2DCoordinates()
            val posY = rand.nextDouble(gVars.sHeight.toDouble()).toB2DCoordinates()
            return addFishToWorld(
                world = world,
                radius = radius,
                angle = angle,
                posX = posX,
                posY = posY
            )
        }
    }

    fun initTexture() {
        img = Texture("SpiralKoi2.png")
        if (rand.nextBoolean()){
            img = Texture("SpiralKoi.png")
        }
        sprite = Sprite(img)
//        sprite.x = body.position.x.toScreenCoordinates() - size / 2
//        sprite.y = body.position.y.toScreenCoordinates() - size / 2
//        sprite.rotation = body.angle
        sprite.setSize(size,size)
//        sprite.setCenter(body.position.x.toScreenCoordinates() - size / 2,body.position.y.toScreenCoordinates() - size / 2)
        sprite.setOriginCenter()
    }

    fun render(batch: Batch) {
//        batch.draw(
//            this.sprite,
//            body.position.x.toScreenCoordinates() - size / 2,
//            body.position.y.toScreenCoordinates() - size / 2,
//            size,
//            size
//        )
//        batch.draw(
//            this.img,
//            this.body.position.x.toScreenCoordinates() - size / 2,
//            this.body.position.y.toScreenCoordinates() - size / 2,
//            size,
//            size
//        )

//        sprite.setPosition(
//            body.position.x.toScreenCoordinates() - size / 2,
//            body.position.y.toScreenCoordinates() - size / 2
//        )
        sprite.x = body.position.x.toScreenCoordinates() - size / 2
        sprite.y = body.position.y.toScreenCoordinates() - size / 2
        sprite.rotation = Math.toDegrees(body.angle.toDouble()).toFloat()
//        sprite.setRotation(body.angle)
        sprite.draw(batch)
//        var asd = sprite.toString().substring(startIndex = 37)
//        info { "Currently drawing $asd and its body pos is: (${body.position.x.toScreenCoordinates()} | ${body.position.y.toScreenCoordinates()})" }
//        info { "Currently drawing  $asd and its sprite pos is: (${sprite.x} | ${sprite.y})" }
//        var koira = 1+1
    }

    fun update(dt: Float) {
        var angle = body.angle
        var veloX = cos(angle) * velocity
        var veloY = sin(angle) * velocity
        body.setLinearVelocity(veloX, veloY)
        donutfyTheWorld()
//        info { "angle: " + body.angle }
    }

    /**
     * When Fish tries to exit too far, this method will bring them back on the other side of the world.
     * Well, its proper name would be screen wrap x and y wise.
     */
    private fun donutfyTheWorld() {
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