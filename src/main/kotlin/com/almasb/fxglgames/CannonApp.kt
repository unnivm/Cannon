package com.almasb.fxglgames

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.text.Text

class CannonApp : GameApplication() {

    var cannon: Entity? = null

    override fun initSettings(settings: GameSettings) {
        with(settings) {
            width = 800
            height = 600
            title = "Cannon Game"
            version = "1.0"
        }
    }

    override fun initGame() {
        val factory = CannonFactory()
        getGameWorld().addEntityFactory(factory)

        initScreenBounds()
        initCannon()
        initBasket()
    }

    override fun initInput() {
        onBtnDown(MouseButton.PRIMARY, "Shoot", { shoot() })
    }

    override fun initGameVars(vars: MutableMap<String, Any>?) {
        vars?.put("score", 0)
    }


    private fun initScreenBounds() {
        entityBuilder().buildScreenBoundsAndAttach(100.0)

    }

    private fun initCannon() {
        cannon = getGameWorld().spawn("cannon", 50.0, getAppHeight() - 300.0)
    }

    private fun initBasket() {
        spawn("basketBarrier", 400.0, getAppHeight() - 300.0)
        spawn("basketBarrier", 700.0, getAppHeight() - 300.0)
        spawn("basketGround", 500.0, getAppHeight().toDouble())
    }

    private fun shoot() {
        spawn("bullet", cannon!!.position.add(70.0, 0.0))
    }

    override fun initPhysics() {
        onCollisionBegin(CannonType.BULLET, CannonType.BASKET, { bullet, basket ->
            bullet.removeFromWorld()
            inc("score", +1000)
        })
    }

    override fun initUI() {
        var scoreText: Text = getUIFactory().newText("", Color.BLACK, 24.0)
        scoreText.translateX = 550.0
        scoreText.translateY = 100.0
        scoreText.textProperty().bind(getGameState().intProperty("score").asString("Score: [%d]"))

        FXGL.getGameScene().addUINode(scoreText)
    }

}

fun main(args: Array<String>) {
    GameApplication.launch(CannonApp::class.java, args)
}