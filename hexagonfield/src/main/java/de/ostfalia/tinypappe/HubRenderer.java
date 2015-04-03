package de.ostfalia.tinypappe;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;


@ResourceDependencies({
        @ResourceDependency(library = "hexagonfield", name = "hub.js", target = "head"),
        @ResourceDependency(library = "hexagonfield", name = "jquery-2.1.3.js", target = "head"),
        @ResourceDependency(library = "javax.faces", name = "jsf.js", target = "head")
}
)
@FacesRenderer(componentFamily = "de.ostfalia.tinypappe.Hub", rendererType = "de.ostfalia.tinypappe.HubRenderer")
public class HubRenderer extends Renderer {

    private static final Logger LOGGER = Logger.getLogger(HubRenderer.class.getName());

    @Override
    public void decode(FacesContext ctx, UIComponent component) {
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        LOGGER.log(Level.INFO, "Encoding: " + component);
        // Path for the websocket connection
        String contextpath = ((HttpServletRequest)context.getExternalContext().getRequest()).getContextPath();

        ResponseWriter writer = context.getResponseWriter();

        Hub hub = (Hub) component;
        writer.startElement("div", component);
        writer.writeAttribute("id", hub.getId(), null);
        writer.endElement("div");

        // Script tag to initialize the JavaScript.
        writer.startElement("script", component);
        writer.write("$( document ).ready(function() {\n" +
                        "    console.log('Hub Starting!');\n"+
                        "    main('"+contextpath+"');\n" +
                     "});");
        writer.endElement("script");
    }
}
