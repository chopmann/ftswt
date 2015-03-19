package de.ostfalia.hexagonfield;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

@FacesComponent("de.ostfalia.HexagonField")
public class HexagonField extends UIComponentBase {

	private static final Logger LOGGER = Logger.getLogger(HexagonField.class.getName());
	public static final String COMPONENT_TYPE = "de.ostfalia.HexagonField";
	
	private enum PropertyKeys {
        width,
        height,
        size,
        fields,
        onClickListener
    }
	
	private Tile[][] tiles;
	private int size = -1;
	private int width = -1;
	private int height = -1;
	
	private boolean initDone;
	
	private List<Tile> selectedTiles;
	
	private OnClickListener onClickListener;
	
	public HexagonField() {
		selectedTiles = new ArrayList<Tile>();
	}

	/**
	 * Initialized the HexagonField.
	 * Sets the values and send the fields with background image to the client side.
	 */
	protected void init() {
		tiles = getTiles();
		size = getSize();
		width = getWidth();
		height = getHeight();
		onClickListener = getOnClickListener();
		
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add(HexagonFieldJsonKey.EVENT.getKey(), HexagonFieldEvent.INIT.getKey())
				.add(HexagonFieldJsonKey.SIZE.getKey(), getSize())
				.add(HexagonFieldJsonKey.INIT.getKey(), true);
		
		/*
		 * Generates an Json array from the Field array.
		 */
		JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();
		JsonArrayBuilder arrayBuilder2;
		for(int y = 0; y < getTiles().length; y++) {
			arrayBuilder2 = Json.createArrayBuilder();
			for(int x = 0; x < getTiles()[y].length; x++) {
                arrayBuilder2.add(Json.createObjectBuilder()
                		.add("background", getTiles()[y][x].getBackgroundImg())
                		.add("foreground", getTiles()[y][x].getForegroundImg()).build());
			}
			arrayBuilder1.add(arrayBuilder2.build());
		}
		
		builder.add(HexagonFieldJsonKey.FIELDS.getKey(), arrayBuilder1.build());
		JsonObject json = builder.build();
		HexagonFieldController.getInstance().sendMessage(this, json.toString());
		
	}

	protected void getContextMenu(int x, int y) {
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for(String str : tiles[y][x].getContextMenu()) {
			arrayBuilder.add(Json.createObjectBuilder()
            		.add("text", str).build());
		}
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add(HexagonFieldJsonKey.EVENT.getKey(), HexagonFieldEvent.CONTEXT_MENU.getKey());
		builder.add(HexagonFieldJsonKey.CONTEXT_MENU.getKey(), arrayBuilder.build());
		builder.add(HexagonFieldJsonKey.FIELD_X.getKey(), x);
		builder.add(HexagonFieldJsonKey.FIELD_Y.getKey(), y);
		JsonObject json = builder.build();
		HexagonFieldController.getInstance().sendMessage(this, json.toString());
	}
	
	/**
	 * User clicked on field position x/y.
	 * Also calls the connected listener.
	 * @param x x position
	 * @param y y position
	 */
	protected void clicked(int x, int y) {
		try {
	        if(!tiles[y][x].isSelectable()) {
	            selectedTiles.add(tiles[y][x]);
	            removeFieldFromSelected(x, y);
	            return;
	        }
			if(!selectedTiles.contains(tiles[y][x])) {
				selectedTiles.add(tiles[y][x]);
				LOGGER.log(Level.INFO, "Clicked " + x + " / " + y);
			}
			/*if(onClickListener != null)
				onClickListener.onClick(x, y);
			else
				LOGGER.log(Level.WARNING, this + " has no onClickListener binded!");*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the field with the position x/y to the selected list.
	 * @param x x position from the field
	 * @param y y position from the field
	 * @return Returns true if the field could be set and false when the field was already set.
	 */
	public boolean addFieldToSelected(int x, int y) {
		if(!selectedTiles.contains(getTiles()[y][x])) {
			selectedTiles.add(getTiles()[y][x]);
			JsonObject json = Json.createObjectBuilder()
					.add(HexagonFieldJsonKey.EVENT.getKey(), HexagonFieldEvent.ADD_SELECTED.getKey())
	    			.add(HexagonFieldJsonKey.FIELD_X.getKey(), x)
	    			.add(HexagonFieldJsonKey.FIELD_Y.getKey(), y)
	    			.build();
			
			HexagonFieldController.getInstance().sendMessage(this, json.toString());
			return true;
		}
		return false;
	}

    /**
     * Removes the field with the position x/y from the selected list.
     * @param x x position from the field
     * @param y y position from the field
     * @return Returns true if the field could be removed and false when the field was already removed.
     */
    public boolean removeFieldFromSelected(int x, int y) {
        if(selectedTiles.contains(tiles[y][x])) {
            selectedTiles.remove(tiles[y][x]);
            JsonObject json = Json.createObjectBuilder()
                    .add(HexagonFieldJsonKey.EVENT.getKey(), HexagonFieldEvent.REMOVE_SELECTED.getKey())
                    .add(HexagonFieldJsonKey.FIELD_X.getKey(), x)
                    .add(HexagonFieldJsonKey.FIELD_Y.getKey(), y)
                    .build();

            HexagonFieldController.getInstance().sendMessage(this, json.toString());
            return true;
        }
        return false;
    }

	/**
	 * Changes the background from the field on the position x / y.
	 * @param x int X position
	 * @param y int Y position
	 * @param path String image path
	 */
	public void changeBackground(int x, int y, String path) {
		tiles[y][x].setBackgroundImg(path);
		JsonObject json = Json.createObjectBuilder()
				.add(HexagonFieldJsonKey.EVENT.getKey(), HexagonFieldEvent.CHANGE_FIELD_BG.getKey())
    			.add(HexagonFieldJsonKey.FIELD_X.getKey(), x)
    			.add(HexagonFieldJsonKey.FIELD_Y.getKey(), y)
    			.add(HexagonFieldJsonKey.BACKGROUND_IMG.getKey(), path)
    			.build();
		
		HexagonFieldController.getInstance().sendMessage(this, json.toString());
	}
	
	/*
	 * ##############################################################################################
	 * #								Component getter & setter									#
	 * ##############################################################################################
	 */

	public int getWidth() {
        return (Integer)getStateHelper().eval(PropertyKeys.width, 0);
    }

	public void setWidth(int width) {
        getStateHelper().put(PropertyKeys.width, width);
    }
    
	public int getHeight() {
        return (Integer)getStateHelper().eval(PropertyKeys.height, 0);
    }

	public void setHeight(int height) {
        getStateHelper().put(PropertyKeys.height, height);
    }
    
	public int getSize() {
        return (Integer)getStateHelper().eval(PropertyKeys.size, 0);
    }

	public void setSize(int size) {
        getStateHelper().put(PropertyKeys.size, size);
    }
    
	public Tile[][] getTiles() {
        return (Tile[][]) getStateHelper().eval(PropertyKeys.fields, null);
    }

	public void setTiles(Tile[][] tiles) {
        getStateHelper().put(PropertyKeys.fields, tiles);
    }
    
	public OnClickListener getOnClickListener() {
		return (OnClickListener) getStateHelper().eval(PropertyKeys.onClickListener, null);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		getStateHelper().put(PropertyKeys.onClickListener, onClickListener);
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_TYPE;
	}

	/*
	 * ##############################################################################################
	 * #										getter & setter										#
	 * ##############################################################################################
	 */
	
	public boolean isInitDone() {
		return initDone;
	}

	public void setInitDone(boolean initDone) {
		this.initDone = initDone;
	}

	public List<Tile> getSelectedTiles() {
		return selectedTiles;
	}

	public void setSelectedTiles(List<Tile> selectedTiles) {
		this.selectedTiles = selectedTiles;
	}
}