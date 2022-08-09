package edu.uob.entities;

import edu.uob.GameEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rooms or places within the game
 */
public class LocationEntity extends GameEntity {
    /**
     * Paths to other locations (note: it is possible for paths to be one-way !)
     */
    private Map<String, LocationEntity> paths;
    /**
     * Characters that are currently at a location
     */
    private Map<String, CharacterEntity> characters;
    /**
     * Artefacts that are currently present in a location
     */
    private Map<String, ArtefactEntity> artefacts;
    /**
     * Furniture that belongs in a location
     */
    private Map<String, FurnitureEntity> furniture;

    private Map<String, PlayerEntity> players;


    public LocationEntity(String name, String description) {
        super(name, description);
        // init the attributes
        paths = new HashMap<>();
        characters = new HashMap<>();
        artefacts = new HashMap<>();
        furniture = new HashMap<>();
        players = new HashMap<>();
    }

    @Override
    public EntityType getType() {
        return EntityType.LOCATION;
    }

    /**
     * handle operation
     */
    /**
     *Add entity to location
     */
    public void add(GameEntity entity){
        EntityType type = entity.getType();
        if (type == EntityType.LOCATION){
            paths.put(entity.getName(), (LocationEntity) entity);
        }else if (type == EntityType.CHARACTER){
            characters.put(entity.getName(), (CharacterEntity) entity);
        }else if (type == EntityType.ARTEFACT){
            artefacts.put(entity.getName(), (ArtefactEntity) entity);
        }else if (type == EntityType.FURNITURE){
            furniture.put(entity.getName(), (FurnitureEntity) entity);
        }else if (type == EntityType.PLAYER){
            players.put(entity.getName(), (PlayerEntity) entity);
        }
    }
    /**
     *Remove entity
     */
    public void remove(GameEntity entity){
        EntityType type = entity.getType();
        Map map = null;
        if (type == EntityType.LOCATION){
            map = paths;
        }else if (type == EntityType.CHARACTER){
            map = characters;
        }else if (type == EntityType.ARTEFACT){
            map = artefacts;
        }else if (type == EntityType.FURNITURE){
            map = furniture;
        }else if (type == EntityType.PLAYER){
            map = players;
        }
        map.remove(entity.getName());
    }
    /**
     *Check if the entity exists in the location
     */
    public boolean contains(GameEntity entity){
        EntityType type = entity.getType();
        Map map = null;
        if (type == EntityType.LOCATION){
            map = paths;
        }else if (type == EntityType.CHARACTER){
            map = characters;
        }else if (type == EntityType.ARTEFACT){
            map = artefacts;
        }else if (type == EntityType.FURNITURE){
            map = furniture;
        }
        return map.containsKey(entity.getName());
    }
    /**
     *Get all things at the location
     */
    public List<GameEntity> get(EntityType type){
        if (type == EntityType.LOCATION){
            return new ArrayList<>(paths.values());
        }else if (type == EntityType.CHARACTER){
            return new ArrayList<>(characters.values());
        }else if (type == EntityType.ARTEFACT){
            return new ArrayList<>(artefacts.values());
        }else if (type == EntityType.FURNITURE){
            return new ArrayList<>(furniture.values());
        }else if (type == EntityType.PLAYER){
            return new ArrayList<>(players.values());
        }
        return null;
    }
    /**
     *Get a thing at the location
     */
    public GameEntity get(EntityType type, String key){
        if (type == EntityType.LOCATION){
            return paths.get(key);
        }else if (type == EntityType.CHARACTER){
            return characters.get(key);
        }else if (type == EntityType.ARTEFACT){
            return artefacts.get(key);
        }else if (type == EntityType.FURNITURE){
            return furniture.get(key);
        }
        return null;
    }
    /**
     *Return a string describing what is at this location
     */

    public String getReport() {
        String reportMsg = new String();
        reportMsg += "You are current in [" + this.getName() + " - " + this.getDescription() + "]\n";
        List<GameEntity> artefacts = this.get(EntityType.ARTEFACT);
        List<GameEntity> furniture = this.get(EntityType.FURNITURE);
        List<GameEntity> characters = this.get(EntityType.CHARACTER);
        List<GameEntity> paths = this.get(EntityType.LOCATION);
        List<GameEntity> players = this.get(EntityType.PLAYER);

        if (artefacts.size() != 0 || furniture.size() != 0 || characters.size() != 0 || paths.size() != 0){
            reportMsg += "You can see:\n";
        }
        for (GameEntity entity : artefacts) {
            reportMsg += "[" + entity.getName() + "] - " + entity.getDescription() + "\n";
        }
        for (GameEntity entity : furniture) {
            reportMsg += "[" + entity.getName() + "] - " + entity.getDescription() + "\n";
        }
        for (GameEntity entity : characters) {
            reportMsg += "[" + entity.getName() + "] - " + entity.getDescription() + "\n";
        }
        reportMsg += "This location has players:" + "\n";
        for (GameEntity entity : players) {
            reportMsg += entity.getName() + "\n";
        }
        if (paths.size() != 0){
            reportMsg += "\nYou can get to these location:\n";
        }
        for (GameEntity entity : paths) {
            reportMsg += "[" + entity.getName() + "] - " + entity.getDescription() + "\n";
        }
        return reportMsg;
    }
}
