package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import ktx.box2d.createWorld

class WorldHolder {
    private val fishList = ArrayList<Fish>()
    val world = createWorld(gravity = Vector2.Zero, true)

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
        fishList.add(Fish.addFishToWorld(world))
    }
}