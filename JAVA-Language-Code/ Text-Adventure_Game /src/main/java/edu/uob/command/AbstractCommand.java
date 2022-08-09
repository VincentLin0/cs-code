package edu.uob.command;

import edu.uob.GameData;
import edu.uob.entities.PlayerEntity;

public abstract class AbstractCommand {
    protected GameData data;

    public AbstractCommand(GameData gameData){

        data = gameData;
    }

    public abstract String handle(PlayerEntity player, String args);
}
