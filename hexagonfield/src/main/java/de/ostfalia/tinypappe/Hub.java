package de.ostfalia.tinypappe;

/**
 * Created by sirmonkey on 4/2/15.
 */
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import java.util.logging.Logger;

@FacesComponent("de.ostfalia.tinypappe.Hub")
public class Hub extends UIComponentBase{
    private static final Logger LOGGER = Logger.getLogger(Hub.class.getName());
    public static final String COMPONENT_TYPE = "de.ostfalia.tinypappe.Hub";

    @Override
    public String getFamily() {
        return COMPONENT_TYPE;
    }

    public SessionController getSessionController() {
        return SessionController.getInstance();
    }
}
