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

    private val gVars = GlobalVariables
    private val world = createWorld(gravity = Vector2.Zero, true)
    private val fishNameMap = HashMap<String, Fish>(gVars.amountOfFishes)



//    init {
//        world.setContactListener(MyContactListener())
//        addWalls()
//    }

    companion object WorldObject {
        val worldHolder = WorldHolder()

        fun findAFish(name:String): Fish? {
            return worldHolder.fishNameMap[name]
        }

        fun findAllFishes(): MutableCollection<Fish> {
            return worldHolder.fishNameMap.values
        }
    }

    fun render(batch: Batch) {
        for (fish: Fish in fishNameMap.values) {
            fish.render(batch)
        }
    }

    fun update(dt: Float) {
        val dt2 = dt * gVars.dtMultiplier
        world.step(dt2, 6, 2)
        for (fish: Fish in fishNameMap.values) {
            fish.update()
        }
    }

    fun addSchoolOfFishToWorld() {
        for (i in 1..gVars.amountOfFishes) {
            val fishe = Fish.addRandomFishToWorld(world)
            fishNameMap[fishe.name] = fishe
        }
    }

    private fun addWalls() {
        val standardOffSize = 50f
        //Vertical walls
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
                filter.categoryBits = gVars.wallExtra.toShort()
                filter.maskBits = (gVars.fishFilter or gVars.repulseFilter or gVars.wallFilter).toShort()
            }
        }
    }

    fun dispose(){
        world.dispose()
    }
}