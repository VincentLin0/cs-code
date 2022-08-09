package edu.uob.entities;

import edu.uob.GameEntity;

/**
 * The various creatures or people involved in game
 */
public class CharacterEntity extends GameEntity {
    public CharacterEntity(String name, String description) {
        super(name, description);
    }

    @Override
    public EntityType getType() {
        return EntityType.CHARACTER;
    }
}
