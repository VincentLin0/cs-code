package edu.uob.command;

import edu.uob.GameAction;
import edu.uob.GameData;
import edu.uob.entities.PlayerEntity;

import java.util.List;
import java.util.Set;

/**
 * default command to handle action or others unrecognized command
 */
public class DefaultCommand extends AbstractCommand{
    public DefaultCommand(GameData gameData) {
        super(gameData);
    }

    @Override
    public String handle(PlayerEntity player, String args) {
        String retMsg = new String();
        String[] argsSplit = args.split(" ");
        if (argsSplit.length <= 1){
            return "Unrecognized command\n";
        }
        if (!isAction(argsSplit)) {
            return "Unrecognized command\n";
        }
        GameAction gameAction = getAction(argsSplit);
        if (gameAction == null){
            return "Unrecognized command\n";
        }

        if (!gameAction.checkConsume(player)) {
            retMsg += "You don't have enough artefacts to do this action\n";
            return retMsg;
        }

        gameAction.doConsume(player);
        gameAction.doProduce(player);
        retMsg += gameAction.getNarration() + "\n";
        if (player.getHealth() <= 0){
            player.dead(this.data.getStartPointLocation());
            retMsg += "You died and lost all of your items, you must return to the start of the game\n";
        }
        return retMsg;
    }

    private GameAction getAction(String[] args) {
        Set<GameAction> actions = this.data.getActions();
        for (GameAction action : actions){
            Set<String> triggers = action.getTriggers();
            for (String trigger : triggers){
                if (contains(args, trigger)){
                    for (String subject : action.getSubjects()){
                        if (contains(args, subject)){
                            return action;
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean isAction(String[] args){
        Set<GameAction> actions = this.data.getActions();
        for (GameAction action : actions){
            Set<String> triggers = action.getTriggers();
            for (String trigger : triggers){
                if (contains(args, trigger)){
                    for (String subject : action.getSubjects()){
                        if (contains(args, subject)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean contains(String[] args, String find){
        boolean contains = false;
        for (String str : args){
            if (str.equalsIgnoreCase(find)){
                contains =  true;
            }
        }
        return contains;
    }
}
