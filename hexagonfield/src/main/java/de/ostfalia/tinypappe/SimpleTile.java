package de.ostfalia.tinypappe;

import java.util.ArrayList;
import java.util.List;

public class SimpleTile implements Tile {
	private int x;
	private int y;
	
	private String backgroundImg;
    private String foregroundImg;

    private boolean selectable;
    
    private List<String> contextMenu;

	public SimpleTile(int x, int y) {
		this.x = x;
		this.y = y;
        backgroundImg = "";
        foregroundImg = "";
        contextMenu = new ArrayList<>();
	}

    @Override
	public int getX() {
		return x;
	}
    @Override
	public void setX(int x) {
		this.x = x;
	}
    @Override
	public int getY() {
		return y;
	}
    @Override
	public void setY(int y) {
		this.y = y;
	}
    @Override
	public String getBackgroundImg() {
		return backgroundImg;
	}
    @Override
	public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
	}

    @Override
    public String getForegroundImg() {
        return foregroundImg;
    }

    @Override
    public void setForegroundImg(String foregroundImg) {
        this.foregroundImg = foregroundImg;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public FieldEvent setSelectable(boolean selectable) {
        this.selectable = selectable;
        FieldEvent fieldEvent = new FieldEvent();
        if(selectable)
        	fieldEvent.eventType = HexagonFieldEvent.ADD_SELECTED;
        else 
        	fieldEvent.eventType = HexagonFieldEvent.REMOVE_SELECTED;
        fieldEvent.tile = this;
        return fieldEvent;
    }

	@Override
	public List<String> getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(List<String> contextMenu) {
		this.contextMenu = contextMenu;
	}
}