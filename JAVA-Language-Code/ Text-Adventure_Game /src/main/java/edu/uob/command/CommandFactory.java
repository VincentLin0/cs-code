package edu.uob.command;

import edu.uob.GameData;

public class CommandFactory {
    public AbstractCommand getCommand(GameData data, String commandName){
        if (commandName.equalsIgnoreCase("inv")) {
            return new InventoryCommand(data);
        }else if (commandName.equalsIgnoreCase("inventory")){
            return new InventoryCommand(data);
        }else if (commandName.equalsIgnoreCase("get")){
            return new GetCommand(data);
        }else if (commandName.equalsIgnoreCase("drop")){
            return new DropCommand(data);
        }else if (commandName.equalsIgnoreCase("goto")){
            return new GotoCommand(data);
        }else if (commandName.equalsIgnoreCase("look")){
            return new LookCommand(data);
        }else if(commandName.equalsIgnoreCase("health")){
            return new HealthCommand(data);
        }
        return null;
    }
}
