package de.ostfalia.tinypappe;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import java.util.logging.Logger;

/**
 * Created by sirmonkey on 4/2/15.
 */
@FacesComponent("de.ostfalia.tinypappe.SidePanel")
public class SidePanel extends UIComponentBase {
    private static final Logger LOGGER = Logger.getLogger(SidePanel.class.getName());
    public static final String COMPONENT_TYPE = "de.ostfalia.tinypappe.SidePanel";

    @Override
    public String getFamily() {
        return COMPONENT_TYPE;
    }
}
