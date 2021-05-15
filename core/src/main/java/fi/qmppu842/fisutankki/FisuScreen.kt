package fi.qmppu842.fisutankki

import ktx.app.KtxScreen

/** First screen of the application. Displayed after the application is created.  */
class FirstScreen : KtxScreen {
    override fun show() {
        // Prepare your screen here.
    }

    override fun render(delta: Float) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    }

    override fun resize(width: Int, height: Int) {
        // Resize your screen here. The parameters represent the new window size.
    }

    override fun pause() {
        // Invoked when your application is paused.
    }

    override fun resume() {
        // Invoked when your application is resumed after pause.
    }

    override fun hide() {
        // This method is called when another screen replaces this one.
    }

    override fun dispose() {
        // Destroy screen's assets here.
    }
}