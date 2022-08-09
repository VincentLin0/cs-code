package edu.uob.command;

import edu.uob.GameData;
import edu.uob.entities.PlayerEntity;

/**
 * describes user's health level
 */
public class HealthCommand extends AbstractCommand{
    public HealthCommand(GameData gameData) {
        super(gameData);
    }

    @Override
    public String handle(PlayerEntity player, String args) {
        int health = player.getHealth();
        return "You heath is " + health + " at this time\n";
    }
}
