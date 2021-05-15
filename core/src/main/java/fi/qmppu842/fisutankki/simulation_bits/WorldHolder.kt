package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.createWorld

class WorldHolder {
    private val fishList = ArrayList<Fish>()
    private val gravity = Vector2.Zero
    val world = createWorld(gravity = gravity, true)

    companion object WorldObject {
        val worldHolder = WorldHolder()
    }

    fun render(dt: Float) {

    }

    fun update(dt: Float) {
        world.step(dt, 6, 2)
    }

    fun addFishToWorld() {
        val radius = 100f
        val body: Body = world.body{
            circle(radius = radius){
                restitution =0.5f
            }
        }
        val fisu = Fish(body)
        fishList.add(fisu)
    }
}