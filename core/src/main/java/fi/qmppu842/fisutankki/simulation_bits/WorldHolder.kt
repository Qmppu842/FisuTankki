package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.ScreenUtils
import fi.qmppu842.fisutankki.GlobalVariables
import fi.qmppu842.fisutankki.toB2DCoordinates
import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.createWorld

class WorldHolder {
    private val fishList = ArrayList<Fish>()
    val world = createWorld(gravity = Vector2.Zero, true)

    private val gVars = GlobalVariables

    companion object WorldObject {
        val worldHolder = WorldHolder()
    }

    init {
        addFishToWorld()
    }

    fun render(batch: Batch) {
        for (fish:Fish in fishList){
            fish.render(batch)
        }
        ScreenUtils.clear(1f, 0f, 0f, 1f)
    }

    fun update(dt: Float) {
        world.step(dt, 6, 2)
        for (fish:Fish in fishList){
            fish.update(dt)
        }
    }

    private fun addFishToWorld() {
        val  radius = 25f
        val body: Body = world.body {
            position.set(gVars.sWidth.toB2DCoordinates() /2, gVars.sHeight.toB2DCoordinates() / 2)
            type = BodyDef.BodyType.DynamicBody
            circle(radius = radius.toB2DCoordinates()) {
                restitution = 0.5f

            }
        }
        val fisu = Fish(body, radius*2)
        fisu.initTexture()
        fishList.add(fisu)
    }
}