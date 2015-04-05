package de.ostfalia.tinypappe;

/**
 * Created by sirmonkey on 4/2/15.
 */

import javafx.geometry.Side;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ResourceDependencies({
        @ResourceDependency(library = "hexagonfield", name = "hub.js", target = "head"),
        @ResourceDependency(library = "hexagonfield", name = "sidepanel.js", target = "head"),
        @ResourceDependency(library = "hexagonfield", name = "hexagon.js", target = "head"),
        @ResourceDependency(library = "hexagonfield", name = "board.js", target = "head"),
        @ResourceDependency(library = "javax.faces", name = "jsf.js", target = "head")
})

@FacesRenderer(componentFamily = "de.ostfalia.tinypappe.SidePanel", rendererType = "de.ostfalia.tinypappe.SidePanelRenderer")
public class SidePanelRenderer extends Renderer {

    private static final Logger LOGGER = Logger.getLogger(SidePanelRenderer.class.getName());

    @Override
    public void decode(FacesContext ctx, UIComponent component) {
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        LOGGER.log(Level.INFO, "Encoding: " + component);

        ResponseWriter writer = context.getResponseWriter();


        SidePanel sidePanel = (SidePanel) component;
        writer.startElement("div", component);
        writer.writeAttribute("id", "SidePanel", null);
        // HTML5 Canvas
        writer.startElement("canvas", component);
        writer.writeAttribute("id", "cv", null);
        writer.writeAttribute("width", 500, null);
        writer.writeAttribute("height", 500, null);
        writer.endElement("canvas");
        writer.endElement("div");



        // Script tag to initialize the JavaScript.
        writer.startElement("script", component);
        writer.write("blueprints.push(SidePanelBuilder);\n");
        writer.endElement("script");
    }
}
