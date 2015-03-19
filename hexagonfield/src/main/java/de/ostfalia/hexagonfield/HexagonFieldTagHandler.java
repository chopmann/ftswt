package de.ostfalia.hexagonfield;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.TagAttribute;

public class HexagonFieldTagHandler extends ComponentHandler {

	private static final String ATTR_WIDTH = "width";
	private static final String ATTR_HEIGHT = "height";
	
	private static final String ATTR_SIZE = "size";
	
	private static final String ATTR_FIELDS = "fields";
	
	private static final String ATTR_ON_CLICK_LISTENER = "onClickListener";
	
	private TagAttribute width;
	private TagAttribute height;
	private TagAttribute size;
	private TagAttribute fields;
	private TagAttribute onClickListener;
	
	public HexagonFieldTagHandler(ComponentConfig config) {
		super(config);
		this.width = getAttribute(ATTR_WIDTH);
		this.height = getAttribute(ATTR_HEIGHT);
		this.size = getAttribute(ATTR_SIZE);
		this.fields = getAttribute(ATTR_FIELDS);
		this.onClickListener = getAttribute(ATTR_ON_CLICK_LISTENER);
	}

}