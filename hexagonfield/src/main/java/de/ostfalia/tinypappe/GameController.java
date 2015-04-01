package de.ostfalia.tinypappe;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;


public class GameController {
	
	private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
	
	private static GameController instance = new GameController();
	
	/**
	 * All available websocketsessions which are not connected to an Board.
	 */
	private List<Session> sessions;
	
	/**
	 * All HexagonFields which are connected to an websocket session. 
	 */
	private Map<Board, Session> map;
	
	private GameController() {
		sessions = new ArrayList<Session>();
		map = new HashMap<Board, Session>();
	}

	/**
	 * Adds the websocket session to the list.
	 * This sessions are currently not connected to any Board.
	 * @param session Session to add.
	 */
	public void addSession(Session session) {
		sessions.add(session);
	}
	
	/**
	 * Connects the session with the sessionId form the list with the Board.
	 * @param board Board which will connected to the session with the id from sessionId.
	 * @param sessionId Session id
	 */
	public void connectSession(Board board, String sessionId) {
		Session session = null;
		for(int i = 0; i < sessions.size() && session == null; i++) {
			if(sessions.get(i).getId().equals(sessionId)) {
				session = sessions.get(i);
			}
		}
        map.put(board, session);
        sessions.remove(session);
		LOGGER.log(Level.INFO, "HexagonController mapped Board " + board + " with session " + sessionId);
		
		board.init();
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
	 * Returns the Board which is connected to the session.
	 * @param session Session
	 * @return Board from the session
	 */
	public Board getHexagonField(Session session) {
		for(Entry<Board, Session> entry : map.entrySet()) {
            boolean exp = entry.getValue().equals(session);
			if(exp) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public List<Board> getAllHexagonFields() {
		return new ArrayList(Arrays.asList(map.keySet().toArray()));
	}
	
	/**
	 * Sends an message over the websocket to the Board.
	 * @param board Board to send to.
	 * @param msg String message to send.
	 */
	public void sendMessage(Board board, String msg) {
        LOGGER.info("Board count: " + map.keySet().size());
        for(Entry<Board, Session> entry : map.entrySet()) {
            Session session = entry.getValue();
            if(session != null && session.isOpen()) {
                RemoteEndpoint.Basic endpoint = session.getBasicRemote();
                try {
                    LOGGER.info("HexagonController send " + msg + " to session " + session.getId());
                    endpoint.sendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                LOGGER.log(Level.WARNING, "Board " + board + " has no websocket session.");
            }
        }
	}
	
	/**
	 * Receives the messages from the websocket endpoint and forward it to the Board.
	 * @param session Session the message came from.
	 * @param msg Message.
	 */
	public void receivedMessage(Session session, Map<HexagonFieldJsonKey, Object> msg) {
		Board board = getHexagonField(session);
		HexagonFieldEvent event = HexagonFieldEvent.fromString((String) msg.get(HexagonFieldJsonKey.EVENT));
		int x;
		int y;
        LOGGER.info("Inc Msg: "+ msg.get(HexagonFieldJsonKey.EVENT));
		switch (event) {
		case INIT:
			board.setInitDone((boolean)msg.get(HexagonFieldJsonKey.VALUE));
            break;
		case CLICKED:
			x = (int) msg.get(HexagonFieldJsonKey.FIELD_X);
			y = (int) msg.get(HexagonFieldJsonKey.FIELD_Y);
			board.clicked(x, y);
			break;
		case ADD_SELECTED:
			break;
		case CHANGE_FIELD_BG:
			break;
		case CONTEXT_MENU:
			x = (int) msg.get(HexagonFieldJsonKey.FIELD_X);
			y = (int) msg.get(HexagonFieldJsonKey.FIELD_Y);
			board.getContextMenu(x, y);
			break;
		default:
			break;
		}
	}
	
	public static GameController getInstance() {
		return instance;
	}
}