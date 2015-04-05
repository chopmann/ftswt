package de.ostfalia.tinypappe;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.TagAttribute;

/**
 * Created by sirmonkey on 4/3/15.
 */
public class SidePanelTagHandler extends ComponentHandler {

    private static final String ATTR_COMPONENT_TYPE="componen_type";
    private TagAttribute componen_type;

    public SidePanelTagHandler(ComponentConfig config) {
        super(config);
        this.componen_type = getAttribute(ATTR_COMPONENT_TYPE);
    }
}
