package de.ostfalia.hexagonfield;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;


public class HexagonFieldController {
	
	private static final Logger LOGGER = Logger.getLogger(HexagonFieldController.class.getName());
	
	private static HexagonFieldController instance = new HexagonFieldController(); 
	
	/**
	 * All available websocketsessions which are not connected to an HexagonField.
	 */
	private List<Session> sessions;
	
	/**
	 * All HexagonFields which are connected to an websocket session. 
	 */
	private Map<HexagonField, Session> map;
	
	private HexagonFieldController() {
		sessions = new ArrayList<Session>();
		map = new HashMap<HexagonField, Session>();
	}
	
	/**
	 * Adds the websocket session to the list.
	 * This sessions are currently not connected to any HexagonField.
	 * @param session Session to add.
	 */
	public void addSession(Session session) {
		sessions.add(session);
	}
	
	/**
	 * Connects the session with the sessionId form the list with the HexagonField. 
	 * @param hexagonField HexagonField which will connected to the session with the id from sessionId.
	 * @param sessionId Session id
	 */
	public void connectSession(HexagonField hexagonField, String sessionId) {
		Session session = null;
		for(int i = 0; i < sessions.size() && session == null; i++) {
			if(sessions.get(i).getId().equals(sessionId)) {
				session = sessions.get(i);
			}
		}
		map.put(hexagonField, session);
		sessions.remove(session);
		LOGGER.log(Level.INFO, "HexagonController mapped HexagonField " + hexagonField + " with session " + sessionId);
		
		hexagonField.init();
	}
	
	/**
	 * Removes the session from the map and session list.
	 * @param session
	 */
	protected void removeSession(Session session) {
		map.values().remove(session);
		sessions.remove(session);
	}
	
	/**
	 * Returns the HexagonField which is connected to the session.
	 * @param session Session
	 * @return HexagonField from the session
	 */
	public HexagonField getHexagonField(Session session) {
		for(Entry<HexagonField, Session> entry : map.entrySet()) {
			if(session.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public List<HexagonField> getAllHexagonFields() {
		return new ArrayList(Arrays.asList(map.keySet().toArray()));
	}
	
	/**
	 * Sends an message over the websocket to the HexagonField.
	 * @param hexagonField HexagonField to send to.
	 * @param msg String message to send.
	 */
	public void sendMessage(HexagonField hexagonField, String msg) {
		Session session = map.get(hexagonField);
		if(session != null && session.isOpen()) {
			RemoteEndpoint.Basic endpoint = session.getBasicRemote();
			try {
				LOGGER.log(Level.FINE, "HexagonController send " + msg + " to session " + session.getId());
				endpoint.sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.log(Level.WARNING, "HexagonField " + hexagonField + " has no websocket session.");
		}
	}
	
	/**
	 * Receives the messages from the websocket endpoint and forward it to the HexagonField.
	 * @param session Session the message came from.
	 * @param msg Message.
	 */
	public void receivedMessage(Session session, Map<HexagonFieldJsonKey, Object> msg) {
		HexagonField hexagonField = getHexagonField(session);
		HexagonFieldEvent event = HexagonFieldEvent.fromString((String) msg.get(HexagonFieldJsonKey.EVENT));
		int x;
		int y;
        LOGGER.info("WOOP WOOP");
		switch (event) {
		case INIT:
			hexagonField.setInitDone((boolean)msg.get(HexagonFieldJsonKey.VALUE));
            break;
		case CLICKED:
			x = (int) msg.get(HexagonFieldJsonKey.FIELD_X);
			y = (int) msg.get(HexagonFieldJsonKey.FIELD_Y);
			hexagonField.clicked(x, y);
			break;
		case ADD_SELECTED:
			break;
		case CHANGE_FIELD_BG:
			break;
		case CONTEXT_MENU:
			x = (int) msg.get(HexagonFieldJsonKey.FIELD_X);
			y = (int) msg.get(HexagonFieldJsonKey.FIELD_Y);
			hexagonField.getContextMenu(x, y);
			break;
		default:
			break;
		}
	}
	
	public static HexagonFieldController getInstance() {
		return instance;
	}
}