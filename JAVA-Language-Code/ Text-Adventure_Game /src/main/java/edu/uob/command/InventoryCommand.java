package edu.uob.command;

import edu.uob.GameData;
import edu.uob.GameEntity;
import edu.uob.entities.PlayerEntity;

import java.util.List;

/**
 * lists all of the artefacts currently being carried by the player
 */
public class InventoryCommand extends AbstractCommand{
    public InventoryCommand(GameData gameData) {
        super(gameData);
    }

    @Override
    public String handle(PlayerEntity player, String args) {
        String retMsg = new String();
        retMsg += player.getInventoryReport();
        return retMsg;
    }
}
