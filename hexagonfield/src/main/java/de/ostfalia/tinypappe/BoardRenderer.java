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
        @ResourceDependency(library = "javax.faces", name = "jsf.js", target = "head")
}
)
@FacesRenderer(componentFamily = "de.ostfalia.Board", rendererType = "de.ostfalia.HexagonFieldRenderer")
public class BoardRenderer extends Renderer {

    private static final Logger LOGGER = Logger.getLogger(BoardRenderer.class.getName());

    @Override
    public void decode(FacesContext ctx, UIComponent component) {
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        LOGGER.log(Level.INFO, "Encoding: " + component);
        // Path for the websocket connection
        String contextpath = ((HttpServletRequest)context.getExternalContext().getRequest()).getContextPath();

        Board board = (Board) component;
        ResponseWriter writer = context.getResponseWriter();

        // HTML5 Canvas
        writer.startElement("canvas", component);
        writer.writeAttribute("id", board.getId(), null);
        writer.writeAttribute("width", board.getWidth(), null);
        writer.writeAttribute("height", board.getHeight(), null);
        writer.writeAttribute("oncontextmenu", "return false", null);
        writer.write("Browser unterstützt kein HTML5 Canvas");
        writer.endElement("canvas");

        writer.startElement("div", component);
        writer.writeAttribute("id", board.getId() + "_contextMenu", null);
        writer.startElement("table", component);
        writer.writeAttribute("id", board.getId() + "_contextMenuTable", null);
        writer.writeAttribute("border", 0, null);
        writer.writeAttribute("cellpadding", 0, null);
        writer.writeAttribute("cellspacing", 0, null);
        writer.writeAttribute("style", "border: thin solid #808080; cursor: default;", null);
        writer.writeAttribute("bgcolor", "Red", null);
        writer.endElement("table");
        writer.endElement("div");

        // Script tag to initialize the JavaScript.
        writer.startElement("script", component);
        writer.write("components.push('board');\n");
        writer.endElement("script");
    }
}
