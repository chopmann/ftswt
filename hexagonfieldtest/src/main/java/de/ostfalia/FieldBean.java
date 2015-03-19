package de.ostfalia;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import de.ostfalia.hexagonfield.Tile;
import de.ostfalia.hexagonfield.SimpleTile;

@Named
@ApplicationScoped
public class FieldBean {

	private Tile[][] tiles;
	
	@PostConstruct
	public void init() {
		tiles = new Tile[30][30];
		List<String> context = new ArrayList<>();
		context.add("erster");
		context.add("erster2");
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = new SimpleTile(j, i);
				tiles[i][j].setSelectable(true);
				if(Math.random() > 0.5) {
					tiles[i][j].setBackgroundImg("resources/images/sand.jpg");
				} else {
					tiles[i][j].setBackgroundImg("resources/images/grass.jpg");
                    tiles[i][j].setForegroundImg("resources/images/tank.png");
				}
				((SimpleTile) tiles[i][j]).setContextMenu(context);
				//fields[i][j].setSelectable(true);
			}
		}
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}
	
}