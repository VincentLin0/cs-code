package edu.uob.command;

import edu.uob.GameData;
import edu.uob.entities.PlayerEntity;

/**
 * picks up a specified artefact from the current location and adds it into player's inventory
 */
public class GetCommand extends AbstractCommand{
    public GetCommand(GameData gameData) {
        super(gameData);
    }

    @Override
    public String handle(PlayerEntity player, String args) {
        String retMsg = new String();
        String[] argsSplit = args.split(" ");
        if (argsSplit.length > 1){
            return "Get command only accept 1 argument. Did you mean: get " + argsSplit[0] + "?\n";
        }
        retMsg += player.take(argsSplit[0]);
        return retMsg;
    }
}
