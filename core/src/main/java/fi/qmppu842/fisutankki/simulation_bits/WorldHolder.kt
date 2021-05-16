package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.ScreenUtils
import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.createWorld

class WorldHolder {
    private val fishList = ArrayList<Fish>()
    private val gravity = Vector2.Zero
    val world = createWorld(gravity = gravity, true)

    val sWidth = Gdx.graphics.width.toFloat()
    val sHeight = Gdx.graphics.height.toFloat()

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
    }

    fun addFishToWorld() {
        val  radius = 100f
        val body: Body = world.body {
            position.set(sWidth /2, sHeight / 2)
            circle(radius = radius) {
                restitution = 0.5f
            }
        }
        val fisu = Fish(body, radius*2)
        fisu.initTexture()
        fishList.add(fisu)
    }
}