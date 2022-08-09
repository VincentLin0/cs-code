package edu.uob;

import edu.uob.entities.ArtefactEntity;
import edu.uob.entities.EntityType;
import edu.uob.entities.LocationEntity;
import edu.uob.entities.PlayerEntity;

import java.util.*;

public class GameAction {
    private Set<String> triggers;
    private Map<String, GameEntity> subjects;
    private Map<String, GameEntity> consumed;
    private Map<String, GameEntity> produced;
    private String narration;

    public GameAction() {
        triggers = new HashSet<>();
        subjects = new HashMap<>();
        consumed = new HashMap<>();
        produced = new HashMap<>();
    }
    public void addTriggers(String trigger){
        triggers.add(trigger);
    }

    public Set<String> getTriggers() {
        return triggers;
    }

    public void addSubject(GameEntity gameEntity){
        subjects.put(gameEntity.getName(), gameEntity);
    }

    public Set<String> getSubjects() {
        return subjects.keySet();
    }

    public void addConsumed(GameEntity gameEntity){
        consumed.put(gameEntity.getName(), gameEntity);
    }

    public void addProduced(GameEntity gameEntity){
        produced.put(gameEntity.getName(), gameEntity);
    }

    public void setNarration(String string){
        narration = string;
    }

    public String getNarration() {
        return narration;
    }
    /**
     * Input a player to check if it can consume
     */
    public boolean checkConsume(PlayerEntity player){
        List<ArtefactEntity> playerArtefacts = player.getArtefacts();
        LocationEntity location = player.getLocation();
        for (String key : consumed.keySet()) {
            GameEntity gameEntity = consumed.get(key);
            EntityType entityType = gameEntity.getType();
            if (entityType == EntityType.ARTEFACT){
                if (!playerArtefacts.contains(gameEntity)){
                    return false;
                }
            }else if (entityType == EntityType.LOCATION || entityType == EntityType.FURNITURE || entityType == EntityType.CHARACTER){
                if (!location.contains(gameEntity)){
                    return false;
                }
            }else if (entityType == EntityType.HEALTH){
                return true;
            }
        }
        return true;
    }
    /**
     * After checking, consume
     */
    public void doConsume(PlayerEntity player) {
        if (checkConsume(player)){
            for (String key : consumed.keySet()) {
                GameEntity consumedEntity = consumed.get(key);
                EntityType entityType = consumedEntity.getType();
                if (entityType == EntityType.ARTEFACT){
                    player.remove((ArtefactEntity) consumedEntity);
                }else if (entityType == EntityType.HEALTH){
                    player.changeHealth(-1);
                }else if (entityType == EntityType.FURNITURE || entityType == EntityType.LOCATION || entityType == EntityType.CHARACTER){
                    LocationEntity location = player.getLocation();
                    location.remove(consumedEntity);
                }
            }
        }
    }

    public void doProduce(PlayerEntity player) {
        for (String key : produced.keySet()) {
            GameEntity producedEntity = produced.get(key);
            EntityType entityType = producedEntity.getType();
            if (entityType == EntityType.ARTEFACT){
                player.add((ArtefactEntity) producedEntity);
            }else if (entityType == EntityType.HEALTH){
                player.changeHealth(+1);
            }else if (entityType == EntityType.FURNITURE || entityType == EntityType.LOCATION || entityType == EntityType.CHARACTER){
                LocationEntity location = player.getLocation();
                location.add(producedEntity);
            }
        }
    }
}
