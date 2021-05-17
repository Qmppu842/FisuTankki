package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import fi.qmppu842.fisutankki.GlobalVariables
import ktx.box2d.createWorld

class WorldHolder {
    private val fishList = ArrayList<Fish>(20)
    val world = createWorld(gravity = Vector2.Zero, true)
    private val fishNameMap = HashMap<String, Fish>(20)

    private val gVars = GlobalVariables


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
        var dt2 = dt* gVars.dtMultiplier
        world.step(dt2, 6, 2)
        for (fish: Fish in fishList) {
            fish.update(dt2)
        }
    }

    fun addSchoolOfFishToWorld() {
        for (i in 1..10) {
            var fishe = Fish.addRandomFishToWorld(world)
            fishList.add(fishe)
            fishNameMap[fishe.name] = fishe
        }
    }

    fun getFish(name: String): Fish? {
        return fishNameMap[name]
    }
}