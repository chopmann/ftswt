package de.ostfalia;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import de.ostfalia.hexagonfield.Field;
import de.ostfalia.hexagonfield.SimpleField;

@Named
@ApplicationScoped
public class FieldBean {

	private Field[][] fields;
	
	@PostConstruct
	public void init() {
		fields = new Field[30][30];
		List<String> context = new ArrayList<>();
		context.add("erster");
		context.add("erster2");
		for(int i = 0; i < fields.length; i++) {
			for(int j = 0; j < fields[i].length; j++) {
				fields[i][j] = new SimpleField(j, i);
				fields[i][j].setSelectable(true);
				if(Math.random() > 0.5) {
					fields[i][j].setBackgroundImg("resources/images/sand.jpg");
				} else {
					fields[i][j].setBackgroundImg("resources/images/grass.jpg");
                    fields[i][j].setForegroundImg("resources/images/tank.png");
				}
				((SimpleField)fields[i][j]).setContextMenu(context);
				//fields[i][j].setSelectable(true);
			}
		}
	}

	public Field[][] getFields() {
		return fields;
	}

	public void setFields(Field[][] fields) {
		this.fields = fields;
	}
	
}