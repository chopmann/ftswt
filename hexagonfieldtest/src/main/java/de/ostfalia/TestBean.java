package de.ostfalia;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.ostfalia.hexagonfield.Field;
import de.ostfalia.hexagonfield.FieldEvent;
import de.ostfalia.hexagonfield.HexagonField;
import de.ostfalia.hexagonfield.OnClickListener;

@Named
@ViewScoped
public class TestBean implements OnClickListener {

	private HexagonField hex;
	
	private Field[][] fields;
	
	private String posX;
	private String posY;
	
	@Inject
	FieldBean fieldBean;
	
	@Inject
	Event<FieldEvent> event;
	
	@PostConstruct
	public void init() {
		fields = fieldBean.getFields();
		hex = new HexagonField();
	}
	
	
	
	public void addField() {
		try {
			int x = Integer.parseInt(posX);
			int y = Integer.parseInt(posY);
			event.fire(fields[y][x].setSelectable(true));
			//hex.addFieldToSelected(x, y);
		} catch(NumberFormatException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "TestBean: Input is not an integer!");
		}
	}

    public void removeField() {
        try {
            int x = Integer.parseInt(posX);
            int y = Integer.parseInt(posY);
            event.fire(fields[y][x].setSelectable(false));
            //hex.removeFieldFromSelected(x, y);
        } catch(NumberFormatException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "TestBean: Input is not an integer!");
        }
    }
	
	public HexagonField getHex() {
		return hex;
	}

	public void setHex(HexagonField hex) {
		this.hex = hex;
	}

	@Override
	public void onClick(int x, int y) {
		Logger.getAnonymousLogger().log(Level.INFO, "TestBean: clicked " + x + " / " + y);
		if(fields[y][x].getBackgroundImg().equals("resources/images/sand.jpg")) {
			hex.changeBackground(x, y, "resources/images/grass.jpg");
		} else {
			hex.changeBackground(x, y, "resources/images/sand.jpg");
		}
	}

	public Field[][] getFields() {
		return fields;
	}

	public void setFields(Field[][] fields) {
		this.fields = fields;
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