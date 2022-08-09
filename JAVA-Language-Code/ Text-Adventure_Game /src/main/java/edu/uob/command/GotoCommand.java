package edu.uob.command;

import edu.uob.GameData;
import edu.uob.GameEntity;
import edu.uob.entities.EntityType;
import edu.uob.entities.LocationEntity;
import edu.uob.entities.PlayerEntity;

import java.util.List;

/**
 * moves the player to a new location (if there is a path to that location)
 */
public class GotoCommand extends AbstractCommand{
    public GotoCommand(GameData gameData) {
        super(gameData);
    }

    @Override
    public String handle(PlayerEntity player, String args) {
        String[] argsSplit = args.split(" ");
        if (argsSplit.length > 1){
            return "Goto command only accept 1 argument. Did you mean: goto " + argsSplit[0] + "?\n";
        }

        LocationEntity location = player.getLocation();
        LocationEntity destinationLocation = (LocationEntity)location.get(EntityType.LOCATION, argsSplit[0]);
        if (destinationLocation == null){
            return "There are no paths to [" + argsSplit[0] + "] to go to\n";
        }
        player.move(destinationLocation);
        return destinationLocation.getReport();
    }
}
