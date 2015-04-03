package saywhat;

import de.ostfalia.tinypappe.Board;
import de.ostfalia.tinypappe.HexagonController;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class EventController {
	
	List<Board> hexagonfields = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		hexagonfields = HexagonController.getInstance().getAllHexagonFields();
	}
	
/*	public void creditPayment(@Observes FieldEvent event) {
		System.out.println("EVENT");
		switch (event.eventType) {
		case ADD_SELECTED:
			for(Board field : hexagonfields) {
				field.addFieldToSelected(event.tile.getX(), event.tile.getY());
			}
			break;
		case REMOVE_SELECTED:
			for(Board field : hexagonfields) {
				System.out.println("remove");
				field.removeFieldFromSelected(event.tile.getX(), event.tile.getY());
			}
			break;
		default:
			break;
		}
		
	}*/
}