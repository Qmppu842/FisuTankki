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
import ktx.box2d.edge
import ktx.box2d.polygon
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Fish(private val body: Body, private val size: Float) {

    lateinit var img: Texture
    lateinit var sprite: Sprite
    private val gVars = GlobalVariables

    private val velocity = 2f

    val name: String = "kala:" + UUID.randomUUID().toString()

    val toRepulseList = ArrayList<Fish>()
    val toAlignList = ArrayList<Fish>()
    val toAttractList = ArrayList<Fish>()


    companion object FishCurator {

        private val gVars = GlobalVariables
        private val rand = gVars.rand
        val fishFilter = 2
        val attractFilter = 4
        val alignFilter = 8
        val repulseFilter = 16

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
                    filter.categoryBits = fishFilter.toShort()
                    filter.maskBits =
                        (fishFilter or attractFilter or alignFilter or repulseFilter).toShort()
                }
            }

            val fisu = Fish(body, radius * 2)
            fisu.initTexture()
            fisu.addRepulsioSensor()
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
        initDebugBody()
    }

    fun render(batch: Batch) {
        sprite.x = body.position.x.toScreenCoordinates() - size / 2
        sprite.y = body.position.y.toScreenCoordinates() - size / 2
        sprite.rotation = Math.toDegrees(body.angle.toDouble()).toFloat()
        sprite.draw(batch)
    }

    var dtCollect = 0f
    var dtCollect2 = 0f
    var dtCollect3 = 0f

    fun update(dt: Float) {
//        var angle = body.angle//fishHiveMindDirection()
        dtCollect -= dt
        var angle = body.angle
//        if (dtCollect < 0.0f) {
//            dtCollect += 0.5f
        angle = when {
//            toRepulseList.size > 0 -> {
//                calcRepulsion()
//            }
//            toAlignList.size > 0 -> {
//                calcAlignCenter()
//            }
            toAttractList.size > 0 -> {
                calcAttractCenter()
            }
            else -> {
                body.angle
//            }
            }
        }
//        if (dtCollect < 0.0f) {
//            dtCollect += 0.5f
        var veloX = cos(angle) * velocity
        var veloY = sin(angle) * velocity
        body.setLinearVelocity(veloX, veloY)
//        }
//        else{
//            body.linearVelocity = body.linearVelocity
//        }
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

    fun addRepulsioSensor() {
        var sensor = body.circle((size * 1.5).toB2DCoordinates()) {
            isSensor = true
            filter.categoryBits = repulseFilter.toShort()
            filter.maskBits = fishFilter.toShort()
        }
    }

    fun addAlignmentSensor() {
        var sensor = body.circle((size * 3).toB2DCoordinates()) {
            isSensor = true
            filter.categoryBits = alignFilter.toShort()
            filter.maskBits = fishFilter.toShort()
        }
    }

    fun addAttractionSensor() {
        var sensor = body.circle((size * 5.5).toB2DCoordinates()) {
            isSensor = true
            filter.categoryBits = attractFilter.toShort()
            filter.maskBits = fishFilter.toShort()
            userData = this@Fish
        }
    }

    /**
     * Calculates target body angle from global mass average and its own position.
     * Ooo soo cool it works, first try!!
     */
    private fun calcAttractCenter(): Float {
        var hiveMind = localFishHiveMindDirection(toAttractList)
        var targetAngle =
            atan2(hiveMind.second - body.position.y, hiveMind.first - body.position.x) * 180.0 / PI

        debugRepBody.setTransform(
            body.position.x ,
            body.position.y ,
            targetAngle.toFloat()
        )

        return targetAngle.toFloat()
    }

    private fun localFishHiveMindDirection(array: ArrayList<Fish>): Pair<Float, Float> {
        var massX = 0f
        var massY = 0f
        for (fish: Fish in array) {
            var pos = fish.getPosition()
            massX += pos.x
            massY += pos.y
        }
        var avgMassX = massX / array.size
        var avgMassY = massY / array.size
        return Pair(avgMassX, avgMassY)
    }

    private fun calcAlignCenter(): Float {
        var angleSum = 0f
        var cosSum = 0f
        var sinSum = 0f
        for (fish: Fish in toAlignList) {
            angleSum += fish.body.angle
            cosSum += cos(fish.body.angle)
            sinSum += sin(fish.body.angle)
        }
//        return angleSum / toAlignList.size
        return atan2(sinSum, cosSum)
//        return atan2(cosSum, sinSum)
    }

    private fun calcRepulsion(): Float {
        var hiveMind = localFishHiveMindDirection(toRepulseList)
        var targetAngle =
            atan2(
                hiveMind.second - body.position.y,
                hiveMind.first - body.position.x
            ) * 180.0 / PI

//        debugRepBody.setTransform(
//            body.position.x ,//+ veloX.toFloat() * 1.1f,
//            body.position.y ,//+ veloY.toFloat() * 1.1f,
//            targetAngle.toFloat()
//        )

        return targetAngle.toFloat()
    }

    lateinit var debugRepBody: Body

    fun initDebugBody() {

        debugRepBody = WorldHolder.worldHolder.world.body {
//            polygon(
//                Vector2(-size.toB2DCoordinates(), -size.toB2DCoordinates()),
//                Vector2(0f, size.toB2DCoordinates()),
//                Vector2(size.toB2DCoordinates(), -size.toB2DCoordinates())
//            ) {
//                isSensor = true
//                filter.categoryBits = 512.toShort()
//                filter.maskBits = 256.toShort()
//            }
            edge(
                from = Vector2(-size.toB2DCoordinates(), -size.toB2DCoordinates()),
                to = Vector2(size.toB2DCoordinates(), size.toB2DCoordinates())
            ) {
                isSensor = true
                filter.categoryBits = 512.toShort()
                filter.maskBits = 256.toShort()
            }
        }
    }
}