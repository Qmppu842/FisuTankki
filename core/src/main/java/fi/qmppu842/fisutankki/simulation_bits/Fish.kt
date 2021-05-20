package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import fi.qmppu842.fisutankki.GlobalVariables
import fi.qmppu842.fisutankki.nextInRange
import fi.qmppu842.fisutankki.toB2DCoordinates
import fi.qmppu842.fisutankki.toScreenCoordinates
import ktx.box2d.body
import ktx.box2d.circle
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

class Fish(private val body: Body, private val size: Float) {

    lateinit var img: Texture
    lateinit var sprite: Sprite
    private val gVars = GlobalVariables

    private var velocity = 2f

    val name: String = "kala:" + UUID.randomUUID().toString()

    val toRepulseList = ArrayList<Fish>()
    val toAlignList = ArrayList<Fish>()
    val toAttractList = ArrayList<Fish>()

    var bodyAngle = 0f

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
//                this.angle = angle

                circle(radius = radius.toB2DCoordinates()) {
                    restitution = 1.5f
                    density = 1090f
                    filter.categoryBits = fishFilter.toShort()
                    filter.maskBits =
                        (attractFilter or alignFilter or repulseFilter).toShort()
//                        (fishFilter or attractFilter or alignFilter or repulseFilter).toShort()
                }
            }
            val fisu = Fish(body, radius * 2)
            fisu.initTexture()
            fisu.addRepulsioSensor()
            fisu.addAlignmentSensor()
            fisu.addAttractionSensor()
            fisu.addFishToItsOwnSensorLists()
            body.userData = fisu
            fisu.bodyAngle = angle
            return fisu
        }

        fun addRandomFishToWorld(world: World): Fish {
            var bonus = -5
            val radius = rand.nextInt(20 + bonus, 26 + bonus).toFloat()
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

    fun addFishToItsOwnSensorLists() {
        toAlignList.add(this)
        toAttractList.add(this)
        toRepulseList.add(this)
        velocity *= nextInRange(0.8f..1.5f)

        var veloX = cos(bodyAngle) * velocity
        var veloY = sin(bodyAngle) * velocity
        oldVelo = Pair(veloX, veloY)
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
        sprite.rotation = Math.toDegrees(bodyAngle.toDouble()).toFloat()
        sprite.draw(batch)
    }

    private var speedMaxLimit = 3f

    fun update(dt: Float) {
        oldVelo = Pair(body.linearVelocity.x, body.linearVelocity.y)
        oldAttCenter = calcAttractCenter3()
        oldRepulsio = calcRepulsion4()
        var align = calcAlignCenter3()
        var veloX = (oldVelo.first + oldAttCenter.first + oldRepulsio.first + align.first) //* 1.01f
        var veloY =
            (oldVelo.second + oldAttCenter.second + oldRepulsio.second + align.second) //* 1.01f

        //Speed limiter
        var speed = sqrt(veloX * veloX + veloY * veloY)
        if (speed > speedMaxLimit) {
            veloX = (veloX / speed) * speedMaxLimit
            veloY = (veloY / speed) * speedMaxLimit
        }

        body.setLinearVelocity(veloX, veloY)
        donutfyTheWorld()

    }

    var oldAttCenter: Pair<Float, Float> = Pair(0f, 0f)
    var oldRepulsio: Pair<Float, Float> = Pair(0f, 0f)
    var oldVelo: Pair<Float, Float> = Pair(1f, 1f)


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

    private fun getPosition(): Vector2 {
        return body.position
    }

    fun addRepulsioSensor() {
        var sensor = body.circle((size * 2.5).toB2DCoordinates()) {
            isSensor = true
            filter.categoryBits = repulseFilter.toShort()
            filter.maskBits = fishFilter.toShort()
            userData = this@Fish
        }
    }

    fun addAlignmentSensor() {
        var sensor = body.circle((size * 3.5).toB2DCoordinates()) {
            isSensor = true
            filter.categoryBits = alignFilter.toShort()
            filter.maskBits = fishFilter.toShort()
            userData = this@Fish
        }
    }

    fun addAttractionSensor() {
        var sensor = body.circle((size * 4.5).toB2DCoordinates()) {
            isSensor = true
            filter.categoryBits = attractFilter.toShort()
            filter.maskBits = fishFilter.toShort()
            userData = this@Fish
        }
    }


    private fun calcRepulsion3(): Pair<Float, Float> {
//        if (toRepulseList.size <=1) return oldVelo
        var scaler = 1f
        var massX = 0f
        var massY = 0f
        for (fish: Fish in toRepulseList) {
            var pos = fish.getPosition()
            massX += pos.x
            massY += pos.y
        }
        var avgX = massX / toRepulseList.size
        var avgY = massY / toRepulseList.size

        var diffX = (body.position.x - avgX) * scaler
        var diffY = (body.position.y - avgY) * scaler

        return Pair(diffX, diffY)
    }

    private fun calcRepulsion4(): Pair<Float, Float> {
        var scaler = 0.025f
        var massX = 0f
        var massY = 0f
        for (fish: Fish in toRepulseList) {
            var pos = fish.getPosition()
            massX += body.position.x - pos.x
            massY += body.position.y - pos.y
        }
        var diffX = (massX) * scaler
        var diffY = (massY) * scaler

        return Pair(diffX, diffY)
    }

    private fun calcAttractCenter3(): Pair<Float, Float> {
        var scaler = 0.005f
        var massX = 0f
        var massY = 0f
        for (fish: Fish in toAttractList) {
            var pos = fish.getPosition()
            massX += pos.x
            massY += pos.y
        }
        var avgX = massX / toAttractList.size
        var avgY = massY / toAttractList.size

        var diffX = (avgX - body.position.x) * scaler
        var diffY = (avgY - body.position.y) * scaler

        return Pair(diffX, diffY)
    }

    private fun calcAlignCenter3(): Pair<Float, Float> {
        var scaler = 0.1f
        var alignX = 0f
        var alignY = 0f
        for (fish: Fish in toAlignList) {
            var attCent = fish.oldAttCenter
            var rep = fish.oldRepulsio
            alignX += attCent.first + rep.first
            alignY += attCent.second + rep.second
        }

        var avgAlignX = alignX / toAlignList.size
        var avgAlignY = alignY / toAlignList.size

        var omaX = (avgAlignX - oldAttCenter.first - oldRepulsio.first) * scaler
        var omaY = (avgAlignY - oldAttCenter.second - oldRepulsio.second) * scaler
        return Pair(omaX, omaY)
    }

}