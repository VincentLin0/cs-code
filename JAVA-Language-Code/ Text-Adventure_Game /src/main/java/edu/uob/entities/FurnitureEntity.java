package edu.uob.entities;

import edu.uob.GameEntity;

/**
 * Physical things that are an integral part of a location (these can NOT be collected by the player)
 */
public class FurnitureEntity extends GameEntity {
    public FurnitureEntity(String name, String description) {
        super(name, description);
    }

    @Override
    public EntityType getType() {
        return EntityType.FURNITURE;
    }
}
