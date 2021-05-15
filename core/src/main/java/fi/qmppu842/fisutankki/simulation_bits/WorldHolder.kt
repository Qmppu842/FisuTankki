package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.math.Vector2
import ktx.box2d.createWorld

class WorldHolder {
    val fishList = ArrayList<Fish>()
    val gravity = Vector2.Zero
    val world = createWorld(gravity = gravity, true)

    companion object WorldObject {
        val worldHolder = WorldHolder()
    }

    fun render(dt: Float) {

    }

    fun update(dt: Float) {
        world.step(dt, 6, 2)
    }
}