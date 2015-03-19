package de.ostfalia.hexagonfield;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Named;

@Named
@ApplicationScoped
public class EventController {
	
	List<HexagonField> hexagonfields = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		hexagonfields = HexagonFieldController.getInstance().getAllHexagonFields();
	}
	
	public void creditPayment(@Observes FieldEvent event) {
		System.out.println("EVENT");
		switch (event.eventType) {
		case ADD_SELECTED:
			for(HexagonField field : hexagonfields) {
				field.addFieldToSelected(event.field.getX(), event.field.getY());
			}
			break;
		case REMOVE_SELECTED:
			for(HexagonField field : hexagonfields) {
				System.out.println("remove");
				field.removeFieldFromSelected(event.field.getX(), event.field.getY());
			}
			break;
		default:
			break;
		}
		
	}
}