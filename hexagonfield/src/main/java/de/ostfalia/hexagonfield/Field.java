package de.ostfalia.hexagonfield;

import java.util.List;

public interface Field {

	public int getX();

	public void setX(int x);

	public int getY();

	public void setY(int y);

	public String getBackgroundImg();

	public void setBackgroundImg(String backgroundImg);

    public String getForegroundImg();

    public void setForegroundImg(String foregroundImg);

    public boolean isSelectable();
    
    public FieldEvent setSelectable(boolean selectable);
    
    public List<String> getContextMenu();
}