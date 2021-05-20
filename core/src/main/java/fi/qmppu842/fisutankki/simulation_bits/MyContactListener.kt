package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import fi.qmppu842.fisutankki.GlobalVariables

class MyContactListener : ContactListener {
    private val gVars = GlobalVariables

    /** Called when two fixtures begin to touch.  */
    override fun beginContact(contact: Contact?) {
        val a: Fish? = contact?.fixtureA?.body?.userData as Fish?
        val b: Fish? = contact?.fixtureB?.body?.userData as Fish?
        if (a != null && b != null) {
            if (contact?.fixtureA?.filterData?.categoryBits == gVars.senseFilter.toShort()) {
                a.withInSensingRange[b.name] = b
            } else if (contact?.fixtureB?.filterData?.categoryBits == gVars.senseFilter.toShort()) {
                b.withInSensingRange[a.name] = a
            }
        }
    }

    /** Called when two fixtures cease to touch.  */
    override fun endContact(contact: Contact?) {
        val a: Fish? = contact?.fixtureA?.body?.userData as Fish?
        val b: Fish? = contact?.fixtureB?.body?.userData as Fish?
        if (a != null && b != null) {
            if (contact?.fixtureA?.filterData?.categoryBits == gVars.senseFilter.toShort()) {
                a.withInSensingRange.remove(b.name)
            } else if (contact?.fixtureB?.filterData?.categoryBits == gVars.senseFilter.toShort()) {
                b.withInSensingRange.remove(a.name)
            }
        }
    }
    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }

}