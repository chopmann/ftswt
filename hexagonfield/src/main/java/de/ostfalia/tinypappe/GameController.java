package de.ostfalia.tinypappe;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

import de.ostfalia.tinypappe.annotations.MessageReceiver;

import java.util.logging.Logger;

/**
 * Created by sirmonkey on 4/2/15.
 */
public class GameController {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private static GameController instance = new GameController();

    private GameController() {
    }

    private void newGame(Session session) {
        LOGGER.info("New Game for: "+session.getId());
        JsonObject msg = Json.createObjectBuilder()
                .add("cmd", "relay").add("reciepent", "sidepanel").build();
        SessionController.getInstance().sendMessage(session, msg);
    }

    @MessageReceiver(value = "hello")
    public void receive(Session session, JsonObject msg) {
        LOGGER.info(msg.toString());
        switch (msg.getString("action")) {
            case "newGame":
                newGame(session);
        }

    }

    public static GameController  getInstance() {
        return instance;
    }
}