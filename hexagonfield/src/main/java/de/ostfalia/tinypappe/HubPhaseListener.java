package de.ostfalia.tinypappe;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sirmonkey on 4/2/15.
 */
public class HubPhaseListener  implements PhaseListener {

    private static final Logger LOGGER = Logger.getLogger(HubPhaseListener.class.getName());
    @Override
    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext context = phaseEvent.getFacesContext();
        if (context.getPartialViewContext().isAjaxRequest()) {

            UIViewRoot view = context.getViewRoot();
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String source = params.get("javax.faces.source");
            UIComponent comp = view.findComponent(source);
            if(comp instanceof Hub) {
                LOGGER.log(Level.INFO, "Hub Working");
            } else {
                LOGGER.log(Level.INFO, "HexagonFieldPhaseListener ajax not from an Board");
            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {

    }

    @Override
    public PhaseId getPhaseId() {
        return null;
    }
}
