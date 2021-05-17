package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.ScreenUtils
import fi.qmppu842.fisutankki.GlobalVariables
import fi.qmppu842.fisutankki.toB2DCoordinates
import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.createWorld
import kotlin.random.Random

class WorldHolder {
    private val fishList = ArrayList<Fish>()
    val world = createWorld(gravity = Vector2.Zero, true)

    companion object WorldObject {
        val worldHolder = WorldHolder()

    }

    init {
//        addFishToWorld()

    }

    fun render(batch: Batch) {
        for (fish: Fish in fishList) {
            fish.render(batch)
        }
//        ScreenUtils.clear(1f, 0f, 0f, 1f)
    }

    fun update(dt: Float) {
        world.step(dt, 6, 2)
        for (fish: Fish in fishList) {
            fish.update(dt)
        }
    }

    fun addSchoolOfFishToWorld() {
        for (i in 1..20) {
            fishList.add(Fish.addRandomFishToWorld(world))

        }
    }
}