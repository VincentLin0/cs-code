package edu.uob;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import edu.uob.entities.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Game's data will store in this class instance
 */
public class GameData {
    private Map<String, LocationEntity> locations;
    private TreeMap<String, GameEntity> entities;
    private TreeMap<String, HashSet<GameAction>> actions;
    private HealthEntity healthEntitySingleton;
    private Map<String, PlayerEntity> players;

    public GameData(File entitiesFile, File actionsFile) {
        locations = new LinkedHashMap<>();
        actions = new TreeMap<>();
        entities = new TreeMap<>();
        healthEntitySingleton = new HealthEntity("health", "This is the health entity");
        players = new HashMap<>();
        parseEntitiesFile(entitiesFile);
        parseActionsFile(actionsFile);
    }
    /**
     * Read data
     */
    private void parseEntitiesFile(File file){
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(file);
            parser.parse(reader);
            List<Graph> graphs = parser.getGraphs().get(0).getSubgraphs();
            for (Graph graph : graphs) {
                for (Graph subgraph : graph.getSubgraphs()) {
                    Node node = subgraph.getNodes(false).get(0);
                    String locationName = node.getId().getId();
                    String locationDescription = node.getAttribute("description");
                    LocationEntity location = new LocationEntity(locationName, locationDescription);
                    List<Graph> subgraphSubgraphs = subgraph.getSubgraphs();
                    for (Graph subgraphSubgraph : subgraphSubgraphs) {
                        String typeName = subgraphSubgraph.getId().getId();
                        for (Node subgraphNode : subgraphSubgraph.getNodes(false)) {
                            String name = subgraphNode.getId().getId();
                            String description = subgraphNode.getAttribute("description");
                            GameEntity gameEntity = null;
                            if (typeName.equalsIgnoreCase("artefacts")){
                                gameEntity = new ArtefactEntity(name, description);
                            }else if (typeName.equalsIgnoreCase("furniture")){
                                gameEntity = new FurnitureEntity(name, description);
                            }else if (typeName.equalsIgnoreCase("characters")){
                                gameEntity = new CharacterEntity(name, description);
                            }
                            if (gameEntity != null){
                                location.add(gameEntity);
                                entities.put(gameEntity.getName(), gameEntity);
                            }
                        }
                    }
                    locations.put(location.getName(), location);
                    entities.put(location.getName(), location);
                }
                List<Edge> edges = graph.getEdges();
                for (Edge edge : edges) {
                    String departure = edge.getSource().getNode().getId().getId();
                    String destination = edge.getTarget().getNode().getId().getId();
                    LocationEntity departureLocationEntity = locations.get(departure);
                    LocationEntity destinationLocationEntity = locations.get(destination);
                    departureLocationEntity.add(destinationLocationEntity);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File could not be found!");
        } catch (com.alexmerz.graphviz.ParseException e) {
            System.out.println("Parser has some errors!");
        }
    }
    /**
     * Processing input instructions
     */
    private void parseActionsFile(File file){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(file);
            Element root = document.getDocumentElement();
            NodeList actionsNodeList = root.getChildNodes();
            // Get the first action (only the odd items are actually actions - 1, 3, 5 etc.)
            for (int i = 1; i < actionsNodeList.getLength(); ) {
                GameAction gameAction = new GameAction();
                Element action = (Element)actionsNodeList.item(i);
                NodeList triggers = action.getElementsByTagName("triggers");
                NodeList subjects = action.getElementsByTagName("subjects");
                NodeList consumeds = action.getElementsByTagName("consumed");
                NodeList produceds = action.getElementsByTagName("produced");
                String narration = action.getElementsByTagName("narration").item(0).getFirstChild().getTextContent();
                gameAction.setNarration(narration);
                for (int j = 0; j < triggers.getLength(); j++) {
                    Element trigger = (Element) triggers.item(j);
                    NodeList keywords = trigger.getElementsByTagName("keyword");
                    for (int m = 0; m < keywords.getLength(); m++) {
                        String keyword = keywords.item(m).getTextContent();
                        HashSet<GameAction> gameActionHashSet;
                        if (!actions.containsKey(keyword)) {
                            gameActionHashSet = new HashSet<>();
                        }else{
                            gameActionHashSet = actions.get(keyword);
                        }
                        gameActionHashSet.add(gameAction);
                        gameAction.addTriggers(keyword);
                        actions.put(keyword, gameActionHashSet);
                    }
                }
                for (int j = 0; j < subjects.getLength(); j++) {
                    Element subject = (Element) subjects.item(j);
                    NodeList keywords = subject.getElementsByTagName("entity");
                    for (int m = 0; m < keywords.getLength(); m++) {
                        String keyword = keywords.item(m).getTextContent();
                        GameEntity gameEntity = entities.get(keyword);
                        gameAction.addSubject(gameEntity);
                    }

                }
                for (int j = 0; j < consumeds.getLength(); j++) {
                    Element consumed = (Element) consumeds.item(j);
                    NodeList keywords = consumed.getElementsByTagName("entity");
                    for (int m = 0; m < keywords.getLength(); m++) {
                        String keyword = keywords.item(m).getTextContent();
                        GameEntity gameEntity = entities.get(keyword);
                        if (keyword.equalsIgnoreCase("health")){
                            gameEntity = healthEntitySingleton;
                        }
                        gameAction.addConsumed(gameEntity);
                    }

                }
                for (int j = 0; j < produceds.getLength(); j++) {
                    Element produced = (Element) produceds.item(j);
                    NodeList keywords = produced.getElementsByTagName("entity");
                    for (int m = 0; m < keywords.getLength(); m++) {
                        String keyword = keywords.item(m).getTextContent();
                        GameEntity gameEntity = entities.get(keyword);
                        if (keyword.equalsIgnoreCase("health")){
                            gameEntity = healthEntitySingleton;
                        }
                        gameAction.addProduced(gameEntity);
                    }

                }

                i = i + 2;
            }


        } catch(ParserConfigurationException pce) {
            System.out.println("ParserConfigurationException was thrown when attempting to read basic actions file");
        } catch(SAXException saxe) {
            System.out.println("SAXException was thrown when attempting to read basic actions file");
        } catch(IOException ioe) {
            System.out.println("IOException was thrown when attempting to read basic actions file");
        }
    }

    public PlayerEntity getPlayer(String username) {
        if (players.containsKey(username)) {
            return players.get(username);
        }else{
            PlayerEntity playerEntity = new PlayerEntity(username, username + "'s description", getStartPointLocation());
            players.put(username,playerEntity);
            return playerEntity;
        }
    }

    public Set<GameAction> getActions(){
        Set<GameAction> set = new HashSet<>();
        for (String key : actions.keySet()) {
            for (GameAction action : actions.get(key)) {
                set.add(action);
            }
        }
        return set;
    }

    public LocationEntity getStartPointLocation(){
        return locations.values().iterator().next();
    }
}
