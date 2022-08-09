package edu.uob;

import edu.uob.entities.EntityType;

public abstract class GameEntity
{
    String name;
    String description;

    public GameEntity(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public abstract EntityType getType();

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }
}
