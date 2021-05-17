package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import fi.qmppu842.fisutankki.GlobalVariables
import fi.qmppu842.fisutankki.toB2DCoordinates
import fi.qmppu842.fisutankki.toScreenCoordinates
import ktx.box2d.body
import ktx.box2d.circle
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Fish(private val body: Body, private val size: Float) {

    lateinit var img: Texture
    lateinit var sprite: Sprite
    private val gVars = GlobalVariables

    private var velocity = 2f


    val toAvoidList = ArrayList<Fish>()
    val toAlignList = ArrayList<Fish>()
    val toAttractList = ArrayList<Fish>()

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
        private fun addFishToWorld(
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
            fisu.addAvoidanceSensor()
            fisu.addAlignmentSensor()
            fisu.addAttractionSensor()
            body.userData = fisu
            return fisu
        }

        fun addRandomFishToWorld(world: World): Fish {
            val radius = rand.nextInt(20, 26).toFloat()
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
        img = if (rand.nextBoolean()) {
            Texture("SpiralKoi.png")
        } else {
            Texture("SpiralKoi2.png")
        }
        sprite = Sprite(img)
        sprite.setSize(size, size)
        sprite.setOriginCenter()
    }

    fun render(batch: Batch) {
        sprite.x = body.position.x.toScreenCoordinates() - size / 2
        sprite.y = body.position.y.toScreenCoordinates() - size / 2
        sprite.rotation = Math.toDegrees(body.angle.toDouble()).toFloat()
        sprite.draw(batch)
    }

    fun update(dt: Float) {
        var angle = fishHiveMindDirection()
        var veloX = cos(angle) * velocity
        var veloY = sin(angle) * velocity
        body.setLinearVelocity(veloX, veloY)
        donutfyTheWorld()
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

    fun getPosition(): Vector2 {
        return body.position
    }

    /**
     * Calculates target body angle from global mass average and its own position.
     * Ooo soo cool it works, first try!!
     */
    private fun fishHiveMindDirection(): Float {
        var hiveMind = WorldHolder.worldHolder.calcMassCenter()
        var targetAngle =
            atan2(hiveMind.second - body.position.y, hiveMind.first - body.position.x) * 180.0 / PI
        return targetAngle.toFloat()
    }

    fun addAvoidanceSensor() {
        var sensor = body.circle((size * 1.5).toB2DCoordinates()) {
            isSensor = true

        }
    }

    fun addAlignmentSensor() {
        var sensor = body.circle((size * 3).toB2DCoordinates()) {
            isSensor = true
        }
    }

    fun addAttractionSensor() {
        var sensor = body.circle((size * 5.5).toB2DCoordinates()) {
            isSensor = true
        }
    }

}