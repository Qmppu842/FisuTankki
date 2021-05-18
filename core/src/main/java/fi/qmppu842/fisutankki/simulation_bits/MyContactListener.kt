package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import fi.qmppu842.fisutankki.GlobalVariables
import ktx.log.info

class MyContactListener : ContactListener {
    private val gVars = GlobalVariables

    /** Called when two fixtures begin to touch.  */
    override fun beginContact(contact: Contact?) {
        var a: Fish? = contact?.fixtureA?.body?.userData as Fish?
        var b: Fish? = contact?.fixtureB?.body?.userData as Fish?
        if (a != null && b != null) {
            var eka = contact?.fixtureA?.isSensor ?: false
            var toka = contact?.fixtureB?.isSensor ?: false
//            gVars.dtMultiplier = 0.05f
            if (eka) {
                when (contact?.fixtureA?.filterData?.categoryBits) {
                    Fish.repulseFilter.toShort() -> {
                        a!!.toRepulseList.add(b!!)
                    }
                    Fish.alignFilter.toShort() -> {
                        a!!.toAlignList.add(b!!)
                    }
                    Fish.attractFilter.toShort() -> {
                        a!!.toAttractList.add(b!!)
                    }
                }
//                a!!.toAttractList.add(b!!)
            } else if (toka) {
                when (contact?.fixtureB?.filterData?.categoryBits) {
                    Fish.repulseFilter.toShort() -> {
                        b!!.toRepulseList.add(a!!)
                    }
                    Fish.alignFilter.toShort() -> {
                        b!!.toAlignList.add(a!!)
                    }
                    Fish.attractFilter.toShort() -> {
                        b!!.toAttractList.add(a!!)
                    }
                }
//                b!!.toAttractList.add(a!!)
            }
        }
    }

    /** Called when two fixtures cease to touch.  */
    override fun endContact(contact: Contact?) {
        var a: Fish? = contact?.fixtureA?.body?.userData as Fish?
        var b: Fish? = contact?.fixtureB?.body?.userData as Fish?
        if (a != null && b != null) {
            var eka = contact?.fixtureA?.isSensor ?: false
            var toka = contact?.fixtureB?.isSensor ?: false
            gVars.dtMultiplier = 1f
            if (eka) {
                when (contact?.fixtureA?.filterData?.categoryBits) {
                    Fish.repulseFilter.toShort() -> {
                        a!!.toRepulseList.remove(b!!)
                    }
                    Fish.alignFilter.toShort() -> {
                        a!!.toAlignList.remove(b!!)
                    }
                    Fish.attractFilter.toShort() -> {
                        a!!.toAttractList.add(b!!)
                    }
                }
            } else if (toka) {
                when (contact?.fixtureB?.filterData?.categoryBits) {
                    Fish.repulseFilter.toShort() -> {
                        b!!.toRepulseList.remove(a!!)
                    }
                    Fish.alignFilter.toShort() -> {
                        b!!.toAlignList.remove(a!!)
                    }
                    Fish.attractFilter.toShort() -> {
                        b!!.toAttractList.remove(a!!)
                    }
                }
            }
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }

}