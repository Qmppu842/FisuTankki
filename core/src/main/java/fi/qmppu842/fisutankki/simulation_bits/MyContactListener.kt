package fi.qmppu842.fisutankki.simulation_bits

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import ktx.log.info

class MyContactListener : ContactListener {
    /** Called when two fixtures begin to touch.  */
    override fun beginContact(contact: Contact?) {
//        var kala = 1 + 1
//        info { "passinger" }
//        if (contact?.fixtureA != null && contact?.fixtureB != null) {
//
//            var eka = contact.fixtureA?.isSensor ?: false
//            var toka = contact.fixtureB?.isSensor ?: false
//            var ekaFish = (contact.fixtureA.userData as Fish?)
//            var tokaFish = (contact.fixtureB.userData as Fish?)
//            if (ekaFish != null && tokaFish != null) {
//                if (eka) {
//                    ekaFish!!.toAttractList.add(tokaFish!!)
//                } else if (toka) {
//                    tokaFish!!.toAttractList.add(ekaFish!!)
//                }
//            }
//        }

//        if (contact?.fixtureA != null && contact.fixtureB != null) {
//
//        }

        var a: Fish? = contact?.fixtureA?.body?.userData as Fish?
        var b: Fish? = contact?.fixtureB?.body?.userData as Fish?
        if (a != null && b != null) {
            var eka = contact?.fixtureA?.isSensor ?: false
            var toka = contact?.fixtureB?.isSensor ?: false
            if (eka) {
                a!!.toAttractList.add(b!!)
            } else if (toka) {
                b!!.toAttractList.add(a!!)
            }
        }
    }

    /** Called when two fixtures cease to touch.  */
    override fun endContact(contact: Contact?) {
//        if (contact?.fixtureA != null && contact?.fixtureB != null) {
//
//            var eka = contact.fixtureA?.isSensor ?: false
//            var toka = contact.fixtureB?.isSensor ?: false
//            var ekaFish = (contact.fixtureA.userData as Fish?)
//            var tokaFish = (contact.fixtureB.userData as Fish?)
//            if (ekaFish != null && tokaFish != null) {
//                if (eka) {
//                    ekaFish!!.toAttractList.remove(tokaFish!!)
//                } else if (toka) {
//                    tokaFish!!.toAttractList.remove(ekaFish!!)
//                }
//            }
//        }

        var a: Fish? = contact?.fixtureA?.body?.userData as Fish?
        var b: Fish? = contact?.fixtureB?.body?.userData as Fish?
        if (a != null && b != null) {
            var eka = contact?.fixtureA?.isSensor ?: false
            var toka = contact?.fixtureB?.isSensor ?: false
            if (eka) {
                a!!.toAttractList.remove(b!!)
            } else if (toka) {
                b!!.toAttractList.remove(a!!)
            }
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }

}