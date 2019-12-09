package com.almasb.fxglgames

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.ExpireCleanComponent
import com.almasb.fxgl.dsl.components.LiftComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.physics.PhysicsComponent
import com.almasb.fxgl.physics.box2d.dynamics.BodyType
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration

class CannonFactory : EntityFactory {

    @Spawns("cannon")
    public fun newCannon(data: SpawnData): Entity? {
        return entityBuilder().type(CannonType.CANNON).from(data).view(Rectangle(70.0, 30.0, Color.BROWN))
                .with(LiftComponent()
                        .yAxisSpeedDuration(150.0, Duration.seconds(1.0)))
                .build()

    }

    @Spawns("basketBarrier")
    public fun newBasketBarrier(data: SpawnData): Entity? {
        return entityBuilder().type(CannonType.BASKET).from(data).viewWithBBox(Rectangle(100.0, 300.0, Color.RED))
                .with(PhysicsComponent())
                .build()
    }

    @Spawns("bullet")
    public fun newBullet(data: SpawnData): Entity? {

        val physics = PhysicsComponent()
        physics.setFixtureDef(FixtureDef().density(0.05f))
        physics.setBodyType(BodyType.DYNAMIC)

        physics.setOnPhysicsInitialized({
            val mousePosition = FXGL.getInput().mousePositionWorld
            physics.setLinearVelocity(mousePosition.subtract(data.x, data.y).normalize().multiply(800.0))
        })

        return entityBuilder().type(CannonType.BULLET).from(data).viewWithBBox(Rectangle(25.0, 25.0, Color.BLUE))
                .collidable()
                .with(physics)
                .with(ExpireCleanComponent(Duration.seconds(4.0)))
                .build()
    }


    @Spawns("basketGround")
    public fun newBasketGround(data: SpawnData): Entity? {
        return entityBuilder().type(CannonType.BASKET).from(data).viewWithBBox(Rectangle(300.0, 5.0, Color.TRANSPARENT))
                .collidable()
                .with(PhysicsComponent())
                .build()
    }

}