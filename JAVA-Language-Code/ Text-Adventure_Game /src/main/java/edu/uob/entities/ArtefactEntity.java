package edu.uob.entities;

import edu.uob.GameEntity;

/**
 * Physical things within the game that can be collected by the player
 */
public class ArtefactEntity extends GameEntity {
    public ArtefactEntity(String name, String description) {
        super(name, description);
    }

    @Override
    public EntityType getType() {
        return EntityType.ARTEFACT;
    }
}
/**
 *Get enumeration type
 */