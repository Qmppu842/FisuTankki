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
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.*

class Fish(private val body: Body, private val size: Float) {

//    private lateinit var img: Texture
    private lateinit var sprite: Sprite
    private val gVars = GlobalVariables

    val name: String = "kala:" + UUID.randomUUID().toString()

    val withInSensingRange = HashMap<String, Fish>(gVars.amountOfFishes)

    private val repulsionDistance = (size * 2f).toB2DCoordinates()
    private val alignDistance = (size * 3.5f).toB2DCoordinates()
    private val attractDistance = (size * 4.5f).toB2DCoordinates()

    private var speedMaxLimit = 3f

    private var oldAttCenter: Pair<Float, Float> = Pair(0f, 0f)
    private var oldRepulsio: Pair<Float, Float> = Pair(0f, 0f)

    companion object FishCurator {

        private val gVars = GlobalVariables
        private val rand = gVars.rand


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
            posY: Float = gVars.sHeight.toB2DCoordinates() / 2
        ): Fish {

            val body: Body = world.body {
                position.set(posX, posY)
                type = BodyDef.BodyType.DynamicBody

                circle(radius = radius.toB2DCoordinates()) {
                    restitution = 1.5f
                    density = 1090f
                    filter.categoryBits = gVars.fishFilter.toShort()
                    filter.maskBits =
                        (gVars.attractFilter or gVars.alignFilter or gVars.repulseFilter or gVars.wallFilter or gVars.senseFilter).toShort()
//                        (fishFilter or attractFilter or alignFilter or repulseFilter or wallFilter).toShort()
                }
            }
            val fisu = Fish(body, radius * 2)
            fisu.initTexture()
            body.userData = fisu
            return fisu
        }

        fun addRandomFishToWorld(world: World): Fish {
            val bonus = -5
            val radius = rand.nextInt(20 + bonus, 26 + bonus).toFloat()
            val posX = rand.nextDouble(gVars.sWidth.toDouble()).toB2DCoordinates()
            val posY = rand.nextDouble(gVars.sHeight.toDouble()).toB2DCoordinates()
            return addFishToWorld(
                world = world,
                radius = radius,
                posX = posX,
                posY = posY
            )
        }
    }

    init {
        withInSensingRange[name] = this

//        addSensor(size * 5f)
        body.linearDamping = 0f
        body.angularDamping = 0f
        body.userData = this
//            name
    }

    fun initTexture() {
        var img = gVars.getTexture()
        sprite = Sprite(img)
        sprite.setSize(size, size)
        sprite.setOriginCenter()
    }

    fun render(batch: Batch) {
        sprite.x = body.position.x.toScreenCoordinates() - size / 2
        sprite.y = body.position.y.toScreenCoordinates() - size / 2
//        sprite.rotation = Math.toDegrees(bodyAngle.toDouble()).toFloat()
        sprite.draw(batch)
    }


    fun update() {
        val newVelocity = calcNewVelocity()
        var veloX = newVelocity.first
        var veloY = newVelocity.second

        //Speed limiter
        val speed = sqrt(veloX * veloX + veloY * veloY)
        if (speed > speedMaxLimit) {
            veloX = (veloX / speed) * speedMaxLimit
            veloY = (veloY / speed) * speedMaxLimit
        }

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

    private fun getPosition(): Vector2 {
        return body.position
    }

    private fun addSensor(sizeOfSensor: Float, typeOfSensor: Int = gVars.senseFilter) {
        body.circle(sizeOfSensor.toB2DCoordinates()) {
            isSensor = true
            filter.categoryBits = typeOfSensor.toShort()
            filter.maskBits = gVars.fishFilter.toShort()
            userData =
                this@Fish
//                name
        }
    }

    private fun calcNewVelocity(): Pair<Float, Float> {
        val currentX = body.position.x
        val currentY = body.position.y

        val repulsionScalar = 0.3f
        var repulsionSumX = 0f
        var repulsionSumY = 0f

        val alignScalar = 0.5f
        var alignXSum = 0f
        var alignYSum = 0f
        var alignCounter = 0

        val attractScalar = 0.2f
        var attractSumX = 0f
        var attractSumY = 0f
        var attractCounter = 0

        //Swap these to easily switch between the naive and the box2d mix approaches.
//        for (fish in withInSensingRange.values) {
        for (fish in WorldHolder.findAllFishes()) {
            val fishX = fish.getPosition().x
            val fishY = fish.getPosition().y
            val distance = sqrt((currentX - fishX).pow(2) + (currentY - fishY).pow(2))

            if (distance < attractDistance) {
                attractSumX += fishX
                attractSumY += fishY
                attractCounter++
            }

            if (distance < alignDistance) {
                val attractionCenterOld = fish.oldAttCenter
                val repulsionPointOld = fish.oldRepulsio
                alignXSum += attractionCenterOld.first + repulsionPointOld.first
                alignYSum += attractionCenterOld.second + repulsionPointOld.second
                alignCounter++
            }

            if (distance < repulsionDistance) {
                repulsionSumX += currentX - fishX
                repulsionSumY += currentY - fishY
            }
        }

        //Calculate Attraction Center
        val attractionCenterX = ((attractSumX / attractCounter) - currentX) * attractScalar
        val attractionCenterY = ((attractSumY / attractCounter) - currentY) * attractScalar
        oldAttCenter = Pair(attractionCenterX, attractionCenterY)

        //Calculate Repulsion Center
        val repulsionCenterX = repulsionSumX * repulsionScalar
        val repulsionCenterY = repulsionSumY * repulsionScalar
        oldRepulsio = Pair(repulsionCenterX, repulsionCenterY)

        //Calculate Alignment Center
        val alignCenterX =
            ((alignXSum / alignCounter) - oldAttCenter.first - oldRepulsio.first) * alignScalar
        val alignCenterY =
            ((alignYSum / alignCounter) - oldAttCenter.second - oldRepulsio.second) * alignScalar

        val oldVelocity = body.linearVelocity

        val newVelocityX = oldVelocity.x + alignCenterX + repulsionCenterX + attractionCenterX
        val newVelocityY = oldVelocity.y + alignCenterY + repulsionCenterY + attractionCenterY

        return Pair(newVelocityX, newVelocityY)
    }

}