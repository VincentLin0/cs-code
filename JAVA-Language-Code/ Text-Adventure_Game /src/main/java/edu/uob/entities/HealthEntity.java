package edu.uob.entities;

import edu.uob.GameEntity;

public class HealthEntity extends GameEntity {
    public HealthEntity(String name, String description) {
        super(name, description);
    }

    @Override
    public EntityType getType() {
        return EntityType.HEALTH;
    }
}
