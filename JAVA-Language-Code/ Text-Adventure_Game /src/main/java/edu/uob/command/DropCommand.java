package edu.uob.command;

import edu.uob.GameData;
import edu.uob.entities.PlayerEntity;

/**
 * puts down an artefact from player's inventory and places it into the current location
 */
public class DropCommand extends AbstractCommand{
    public DropCommand(GameData gameData) {
        super(gameData);
    }

    @Override
    public String handle(PlayerEntity player, String args) {
        String retMsg = new String();
        String[] argsSplit = args.split(" ");
        if (argsSplit.length > 1){
            return "Drop command only accept 1 argument. Did you mean: drop " + argsSplit[0] + "?\n";
        }
        retMsg += player.drop(argsSplit[0]);
        return retMsg;
    }
}
