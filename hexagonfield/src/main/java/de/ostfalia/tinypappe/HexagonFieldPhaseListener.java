package de.ostfalia.tinypappe;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

@SuppressWarnings("serial")
public class HexagonFieldPhaseListener implements PhaseListener {

	private static final Logger LOGGER = Logger.getLogger(HexagonFieldPhaseListener.class.getName());
	
	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		if (context.getPartialViewContext().isAjaxRequest()) {
			LOGGER.log(Level.INFO, "HexagonPhaseListener process ajax request...");
			
			UIViewRoot view = context.getViewRoot();
		    Map<String, String> params = context.getExternalContext().getRequestParameterMap();
			String source = params.get("javax.faces.source");
			LOGGER.log(Level.INFO, "HexagonPhaseListener source " + source);
			UIComponent comp = view.findComponent(source);
			if(comp instanceof Board) {
				Board hex = (Board) comp;
				if(params.containsKey("sessionId")) {
					LOGGER.log(Level.INFO, "HexagonFieldPhaseListener received session id " + params.get("sessionId") + " for Board " + hex);
					HexagonController.getInstance().connectSession(hex, params.get("sessionId"));
				}
			} else {
				LOGGER.log(Level.INFO, "HexagonFieldPhaseListener ajax not from an Board");
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {   
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}