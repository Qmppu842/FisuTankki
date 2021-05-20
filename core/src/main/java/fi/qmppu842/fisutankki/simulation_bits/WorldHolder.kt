package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import fi.qmppu842.fisutankki.GlobalVariables
import fi.qmppu842.fisutankki.toB2DCoordinates
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.createWorld

class WorldHolder {
    private val fishList = ArrayList<Fish>(20)
    val world = createWorld(gravity = Vector2.Zero, true)
    private val fishNameMap = HashMap<String, Fish>(20)

    private val gVars = GlobalVariables


    init {
        world.setContactListener(MyContactListener())
        addWalls()
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
        var dt2 = dt * gVars.dtMultiplier
        world.step(dt2, 6, 2)
        for (fish: Fish in fishList) {
            fish.update(dt2)
        }
    }

    fun addSchoolOfFishToWorld() {
        for (i in 1..20) {
            var fishe = Fish.addRandomFishToWorld(world)
            fishList.add(fishe)
            fishNameMap[fishe.name] = fishe
        }
    }


    var wallFilter = 32
    private fun addWalls() {
        //Vertical walls
        var standardOffSize = 50f
        addWall(
            x = gVars.sWidth,
            y = gVars.sHeight / 2,
            width = standardOffSize,
            height = gVars.sHeight
        )

        addWall(
            x = 0f,
            y = gVars.sHeight / 2,
            width = standardOffSize,
            height = gVars.sHeight
        )

        //Horizontal walls
        addWall(
            x = gVars.sWidth / 2,
            y = 0f,
            width = gVars.sWidth - standardOffSize-5,
            height = standardOffSize
        )
        addWall(
            x = gVars.sWidth / 2,
            y = gVars.sHeight,
            width = gVars.sWidth - standardOffSize -5,
            height = standardOffSize
        )
    }

    private fun addWall(x: Float, y: Float, width: Float, height: Float) {
        world.body {
            position.set(x.toB2DCoordinates(), y.toB2DCoordinates())
            type = BodyDef.BodyType.StaticBody
            box(width = (width + 2).toB2DCoordinates(), height = (height + 2).toB2DCoordinates()) {
                filter.categoryBits = 31.toShort()
                filter.maskBits = (Fish.fishFilter or Fish.repulseFilter or wallFilter).toShort()
            }
        }
    }
}