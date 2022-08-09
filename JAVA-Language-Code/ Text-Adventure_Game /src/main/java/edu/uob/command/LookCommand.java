package edu.uob.command;

import edu.uob.GameData;
import edu.uob.GameEntity;
import edu.uob.entities.EntityType;
import edu.uob.entities.LocationEntity;
import edu.uob.entities.PlayerEntity;

import java.util.List;

/**
 * describes the entities in the current location and lists the paths to other locations
 */
public class LookCommand extends AbstractCommand{
    public LookCommand(GameData gameData) {
        super(gameData);
    }

    @Override
    public String handle(PlayerEntity player, String args) {
        String retMsg = new String();
        LocationEntity location = player.getLocation();
        retMsg = location.getReport();
        return retMsg;
    }
}
