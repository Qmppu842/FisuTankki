package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold

class MyContactListener : ContactListener {
    /** Called when two fixtures begin to touch.  */
    override fun beginContact(contact: Contact?) {

    }

    /** Called when two fixtures cease to touch.  */
    override fun endContact(contact: Contact?) {
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }

}