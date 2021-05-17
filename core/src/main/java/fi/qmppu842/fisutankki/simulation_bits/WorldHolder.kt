package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import ktx.box2d.createWorld

class WorldHolder {
    private val fishList = ArrayList<Fish>()
    val world = createWorld(gravity = Vector2.Zero, true)

    init {
        world.setContactListener(MyContactListener())
    }

    companion object WorldObject {
        val worldHolder = WorldHolder()
    }

    fun render(batch: Batch) {
        for (fish: Fish in fishList) {
            fish.render(batch)
        }
    }

    fun update(dt: Float) {
        world.step(dt, 6, 2)
        for (fish: Fish in fishList) {
            fish.update(dt)
        }
    }

    fun addSchoolOfFishToWorld() {
        for (i in 1..10) {
            fishList.add(Fish.addRandomFishToWorld(world))
        }
    }

    fun calcMassCenter(): Pair<Float, Float> {
        var massX = 0f
        var massY = 0f
        for (fish: Fish in fishList) {
            var pos = fish.getPosition()
            massX += pos.x
            massY += pos.y
        }
        var avgMassX = massX / fishList.size
        var avgMassY = massY / fishList.size
        return Pair(avgMassX, avgMassY)
    }
}