package de.ostfalia.tinypappe;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

import de.ostfalia.tinypappe.annotations.MessageReceiver;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by sirmonkey on 4/2/15.
 */
public class GameController {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private static GameController instance = new GameController();
    private HashMap<UUID,Game> games ;
    private GameController() {
        games = new HashMap<>();
    }

    private void newGame(Session session) {
        LOGGER.info("New Game for: "+session.getId());
        UUID id = UUID.randomUUID();
        games.put(id, new Game(id));
        JsonObject payload = Json.createObjectBuilder().add("game_id",id.toString()).build();
        JsonObject msg = Json.createObjectBuilder()
                .add("cmd", "relay")
                .add("receiver", "sidepanel")
                .add("action", "joinGame")
                .add("payload",payload)
        .build();
        SessionController.getInstance().sendMessage(session, msg);
    }

    private void joinGame(Session session, JsonObject msg) {
        JsonObject payload = msg.getJsonObject("payload");
        LOGGER.info("Joining Game: " + payload.getString("game_id"));
        JsonObject response = Json.createObjectBuilder()
                .add("cmd", "log")
                .add("receiver", "board")
                .add("action", "init")
                .add("payload",payload)
                .build();
        SessionController.getInstance().sendMessage(session, response);
    }

    @MessageReceiver(value = "hello")
    public void receive(Session session, JsonObject msg) {
        LOGGER.info(msg.toString());
        switch (msg.getString("action")) {
            case "newGame":
                newGame(session);
                break;
            case "joinGame":
                joinGame(session, msg);
                break;
        }

    }

    public static GameController  getInstance() {
        return instance;
    }
}