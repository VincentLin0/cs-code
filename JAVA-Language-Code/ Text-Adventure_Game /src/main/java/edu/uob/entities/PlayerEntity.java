package edu.uob.entities;

import edu.uob.GameEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A special kind of character that represents the user in the game
 */
public class PlayerEntity extends GameEntity {
    private Map<String, ArtefactEntity> artefacts;
    private LocationEntity location;
    private int health;

    public PlayerEntity(String name, String description, LocationEntity locationEntity) {
        super(name, description);
        artefacts = new HashMap<>();
        move(locationEntity);
        health = 3;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }
    /**
     *Get the type of entity
     */

    public void add(ArtefactEntity item){
        artefacts.put(item.getName(), item);
    }

    public void remove(ArtefactEntity item){
        artefacts.remove(item.getName());
    }

    public void dead(LocationEntity locationEntity){
        for (ArtefactEntity artefact : getArtefacts()) {
            location.add(artefact);
        }
        artefacts = new HashMap<>();
        move(locationEntity);
        health = 3;
    }

    public LocationEntity getLocation(){
        return location;
    }

    public List<ArtefactEntity> getArtefacts(){
        return new ArrayList<>(artefacts.values());
    }

    public void move(LocationEntity destinationLocation) {
        if(this.location == null){
            this.location = destinationLocation;
            this.location.add(this);
        }else{
            this.location.remove(this);
            this.location = destinationLocation;
            this.location.add(this);
        }
    }

    public String getInventoryReport() {
        String retMsg = new String();
        List<ArtefactEntity> artefacts = this.getArtefacts();
        if (artefacts.size() == 0){
            retMsg += "You do not have any artefacts in your inventory\n";
        }else{
            retMsg += "You have:\n";
            for (GameEntity artefact : artefacts) {
                retMsg += "[" + artefact.getName() + "] - " + artefact.getDescription() + "\n";
            }
        }
        return retMsg;
    }

    public String take(String artefactName) {
        String retMsg = new String();
        LocationEntity location = this.location;
        ArtefactEntity targetArtefact = (ArtefactEntity)location.get(EntityType.ARTEFACT, artefactName);
        if (targetArtefact == null){
            retMsg += "There are no [" + artefactName + "] to take\n";
            return retMsg;
        }
        this.add(targetArtefact);
        location.remove(targetArtefact);
        retMsg += "Take [" + artefactName + "] successfully\n";
        return retMsg;
    }

    public String drop(String artefactName) {
        String retMsg = new String();
        ArtefactEntity artefactEntity = artefacts.get(artefactName);
        if (artefactEntity == null){
            retMsg += "There are no [" + artefactName + "] to drop\n";
            return retMsg;
        }
        this.remove(artefactEntity);
        location.add(artefactEntity);
        retMsg += "Drop [" + artefactName + "] successfully\n";
        return retMsg;
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int count) {
        health = health + count;
        if (health > 3){
            health = 3;
        }
        if (health < 0){
            health = 0;
        }
    }
}
