package de.ostfalia;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.JsonObject;

import de.ostfalia.tinypappe.*;

@Named
@ViewScoped
public class TestBean implements OnClickListener, Serializable {

	private Board hex;
	
	private Tile[][] tiles;
	
	private String posX;
	private String posY;
	
	@Inject
	FieldBean fieldBean;
	
	@Inject
	Event<FieldEvent> event;
	
	@PostConstruct
	public void init() {
		tiles = fieldBean.getTiles();
		hex = new Board();
	}
	
	
	
	public void addField() {
		try {
			int x = Integer.parseInt(posX);
			int y = Integer.parseInt(posY);
			event.fire(tiles[y][x].setSelectable(true));
			//hex.addFieldToSelected(x, y);
		} catch(NumberFormatException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "TestBean: Input is not an integer!");
		}
	}

    public void removeField() {
        try {
            int x = Integer.parseInt(posX);
            int y = Integer.parseInt(posY);
            event.fire(tiles[y][x].setSelectable(false));
            //hex.removeFieldFromSelected(x, y);
        } catch(NumberFormatException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "TestBean: Input is not an integer!");
        }
    }
	
	public Board getHex() {
		return hex;
	}

	public void setHex(Board hex) {
		this.hex = hex;
	}

	@Override
	public void onClick(int x, int y) {
		Logger.getAnonymousLogger().log(Level.INFO, "TestBean: clicked " + x + " / " + y);
        System.out.println("Clicked");
        if(tiles[y][x].getBackgroundImg().equals("resources/images/sand.jpg")) {
            JsonObject msg = hex.changeBackground(x, y, "resources/images/grass.jpg");
            HexagonController.getInstance().sendMessage(hex, msg.toString());
        } else {
            JsonObject msg = hex.changeBackground(x, y, "resources/images/sand.jpg");
            HexagonController.getInstance().sendMessage(hex, msg.toString());
        }
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public String getPosX() {
		return posX;
	}

	public void setPosX(String posX) {
		this.posX = posX;
	}

	public String getPosY() {
		return posY;
	}

	public void setPosY(String posY) {
		this.posY = posY;
	}
}