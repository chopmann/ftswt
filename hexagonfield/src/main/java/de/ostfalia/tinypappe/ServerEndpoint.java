package de.ostfalia.tinypappe;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@javax.websocket.server.ServerEndpoint(value = "/hexagonfieldnew")
public class ServerEndpoint {

	private static final Logger LOGGER = Logger.getLogger(ServerEndpoint.class.getName());

	/**
	 * Adds on open the session to the controller list and sends to the client the session id.
	 * @param session Session
	 */
	@OnOpen
	public void onOpen(Session session) {
		LOGGER.log(Level.INFO, "HexagonWebsocket new connection: " + session.getId());
        SessionController.getInstance().joinWaitingRoom(session);
        JsonObject msg = Json.createObjectBuilder().add("cmd", "log")
                .add("payload", "heavy").build();
        SessionController.getInstance().sendMessage(session, msg);
	}

	/**
	 * Parses the incomming json string into an HashMap.
	 * @param message Json as String.
	 * @param session Session where the message came from.
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		JsonParser parser = Json.createParser(new StringReader(message));
		Event event;
		HexagonFieldJsonKey key = null;

		Map<HexagonFieldJsonKey, Object> msg = new HashMap<HexagonFieldJsonKey, Object>();

		while (parser.hasNext()) {
			event = parser.next();
			if (event.equals(Event.KEY_NAME)) {
				key = HexagonFieldJsonKey.fromString(parser.getString());
			} else if (event.equals(Event.VALUE_STRING)) {
				if (key == null) {
					LOGGER.log(Level.SEVERE, "No key for JSON value String: " + parser.getString());
				} else {
					if(parser.getString().equals("done")) {
						msg.put(key, true);
					} else {
						msg.put(key, parser.getString());
					}
					key = null;
				}
			} else if (event.equals(Event.VALUE_NUMBER)) {
				if (key == null) {
					LOGGER.log(Level.SEVERE, "No key for JSON value Integer: " + parser.getString());
				} else {
					msg.put(key, parser.getInt());
					key = null;
				}
			}
		}

		HexagonController.getInstance().receivedMessage(session, msg);
	}

	/**
	 * Removes the session from the controller on close.
	 * @param session
	 */
	@OnClose
	public void onClose(Session session) {
		LOGGER.log(Level.INFO, "HexagonWebsocket close connection: " + session.getId());
		HexagonController.getInstance().removeSession(session);
        SessionController.getInstance().leaveWaitingRoom(session);
	}
}