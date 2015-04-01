/**
 * Key's for the json objects.
 */
var HexagonJsonKey = {
	JAVA_SOURCE: "javax.faces.source",
	SESSION_ID: "sessionId",
	EVENT: "event",
	SIZE: "size",
	FIELD_X: "fieldX",
	FIELD_Y: "fieldY",
	BACKGROUND_IMG: "bgImg",
	CONTEXT_MENU: "contextMenu"
};

/**
 * Events that can send or received over the websocket. 
 */
var HexagonEvent = {
	CONNECT_SESSION: "connect",
	INIT: "init",
	ADD_SELECTED: "addSelected",
    REMOVE_SELECTED: "removeSelected",
	CHANGE_FIELD_BG: "changeFieldBg",
	CLICKED: "clicked",
	CONTEXT_MENU: "contextMenu"
};

/**
 * Initialize the canvas, Board and build up the websocket connection.
 * @constructor
 * @param {string} canvas - Canvas to draw in.
 * @param {string} contextpath - Path for the websocket connection.
 */
function HexagonHandler(canvas, contextpath) {
	this.canvas = document.getElementById(canvas);
	this.canvas.addEventListener('contextmenu', ShowMenu.bind(this), false );
	var connection = "ws://" + window.location.host + contextpath + "/hexagonfieldnew";
	this.socket = new WebSocket(connection);
	this.socket.onmessage = socketMessage.bind(this);
    
	this.hexagonField = new HexagonField(
			this.canvas.width,
			this.canvas.height,
			this.canvas);
	this.hexagonField.fieldClicked = fieldClicked.bind(this);
}

/**
 * Receives all messages that come from the websocket.
 * Parses the data into an json object and delegate it based on the event.
 * @param {Object} event - incoming data
 * @this {HexagonHandler}
 */
function socketMessage(event) {
    var json = JSON.parse(event.data);

	if(json.event) {
		console.log(Date.now() + " Msg Received: " + json.event);
		switch(json.event) {
            case HexagonEvent.CONNECT_SESSION:
                connect(json, this);
                break;
            case HexagonEvent.INIT:
                if (!this.hexagonField.InitDone) {
                    initHexagonField(json, this);
                    console.log("KAF")
                } else {
                    console.log("FAK")
                };
                break;
            case HexagonEvent.CHANGE_FIELD_BG:
                changeFieldBackground(json, this);
                break;
            case HexagonEvent.ADD_SELECTED:
                addSelectedField(json, this);
                break;
            case HexagonEvent.REMOVE_SELECTED:
                removeSelectedField(json, this);
                break;
            case HexagonEvent.CONTEXT_MENU:
            	setContextMenu(json, this);
            	openMenu(json, this);
            	break;
		}
	}
}

/**
 * Search from the given element the first parent form.
 * @param {Element || Node} elem - given element.
 * @returns {Node} form element.
 */
function findForm(elem){
	var parent = elem.parentNode; 
	if(parent && parent.tagName != 'FORM'){
		parent = findForm(parent);
	}
	return parent;
}

/**
 * Called when an field was clicked and send the position to the server.
 * @param {number} x - X position
 * @param {number} y - Y position
 */
function fieldClicked(x, y) {
	var json = {};
	json[HexagonJsonKey.EVENT] = HexagonEvent.CLICKED;
	json[HexagonJsonKey.FIELD_X] = x;
	json[HexagonJsonKey.FIELD_Y] = y;
	this.socket.send(JSON.stringify(json));
}

/**
 * Sends an ajax request to the server from the Board element to
 * map the UIComponent with the websocket session.
 * 
 * @param {JSON} data - Json object from the incoming message.
 *  @param {string} data.sessionId - Websocket session id.
 * @param {HexagonHandler} handler - Handler to get the canvas.
 */
function connect(data, handler) {
	var elementId = findForm(handler.canvas).id + ":" + handler.canvas.id;
	var parameter = {};
	parameter[HexagonJsonKey.JAVA_SOURCE] = elementId;
	parameter[HexagonJsonKey.SESSION_ID] = data.sessionId;
	jsf.ajax.request(handler.canvas, null, {
		parameters: parameter
	});
}

/**
 * Initialize the values from the Board and loads all
 * background images after that informs the server the initialization is done.
 * 
 * @param {JSON} data - Json object from incoming message.
 *  @param {Array} data.fields - Array with all fields.
 *   @param {string} data.fields.background - Field background path.
 *   @param {string} data.fields.foreground - Field foreground path.
 *  @param {number} data.size - Size of field side.
 * @param {HexagonHandler} handler - HexagonHandler
 */
function initHexagonField(data, handler) {
	handler.hexagonField.countX = data.fields[0].length;
	handler.hexagonField.countY = data.fields.length;
	handler.hexagonField.size = data.size;
	handler.hexagonField.init();
	
	var imageArray = [];
	var loadedImagesCount = 0;
	var drawn = false;

	for(var i = 0; i < data.fields.length; i++) {
		for(var j = 0; j < data.fields[i].length; j++) {
			var bgImg = new Image();
            var fImg = new Image();
            fImg.src = data.fields[i][j].foreground;
			imageArray.push(bgImg);
			bgImg.src = data.fields[i][j].background;
			bgImg.onload = function(){
				loadedImagesCount++;
				if (loadedImagesCount >= data.fields.length * data.fields.length && !drawn) {
                    handler.hexagonField.draw();
                    drawn = true;
				}
			}.bind(this);
			handler.hexagonField.hexagons[i][j].bgImg = bgImg;
            handler.hexagonField.hexagons[i][j].foregroundImg = fImg;
		}
	}

	handler.socket.send(JSON.stringify({event:HexagonEvent.INIT, value:"done"}));

}

/**
 * Changes the background image from the field on the position fieldX / fieldY.
 * 
 * 
 * @param {JSON} data - Json object from incoming message.
 *  @param {number} data.fieldX - X position
 *  @param {number} data.fieldY - Y position
 *  @param {string} data.bgImg - Path to the image.
 * @param {HexagonHandler} handler - HexagonHandler
 */
function changeFieldBackground(data, handler) {
	var image = new Image();
	image.src = data.bgImg;
	image.onload = function(){
		handler.hexagonField.hexagons[data.fieldY][data.fieldX].bgImg = image;
		handler.hexagonField.draw();
	}.bind(handler);
}

/**
 * Adds an field to the selection from the server side.
 * 
 * @param {JSON} data - Json object from incoming message.
 *  @param {number} data.fieldX - X position
 *  @param {number} data.fieldY - Y position
 * @param {HexagonHandler} handler - HexagonHandler
 */
function addSelectedField(data, handler) {
	handler.hexagonField.selectedFields.push(new Point(data.fieldX, data.fieldY));
	handler.hexagonField.drawSelected();
}

/**
 * Removes an field from the selection from the server side.
 *
 * @param {JSON} data - Json object from incoming message.
 *  @param {number} data.fieldX - X position
 *  @param {number} data.fieldY - Y position
 * @param {HexagonHandler} handler - HexagonHandler
 */
function removeSelectedField(data, handler) {
    handler.hexagonField.removeSelection(new Point(data.fieldX, data.fieldY));
}

/**
 * Set the context menu.
 */
function setContextMenu(data, handler) {
	handler.hexagonField.setContextMenu(new Point(data.fieldX, data.fieldY), data[HexagonEvent.CONTEXT_MENU]);
}

/**
 * Requests the context menu for clicked field.
 */
function ShowMenu(e) {
	var found = false;
	for(var i = this.hexagonField.visibleStart.y; i < this.hexagonField.hexagons.length && i < this.hexagonField.visibleEnd.y && !found; i++) {
		for(var j = this.hexagonField.visibleStart.x; j < this.hexagonField.hexagons[i].length && j < this.hexagonField.visibleEnd.x && !found; j++) {
			if(this.hexagonField.hexagons[i][j].isPointIn(new Point(e.layerX, e.layerY))) {
               console.log(j + " / " + i);
               var json = {};
	           	json[HexagonJsonKey.EVENT] = HexagonEvent.CONTEXT_MENU;
	           	json[HexagonJsonKey.FIELD_X] = j;
	           	json[HexagonJsonKey.FIELD_Y] = i;
	           	this.socket.send(JSON.stringify(json));
	           	this.openContextMenu = true;
	           	this.contextX = e.layerX;
	           	this.contextY = e.layerY;
			}
		}
	}

}

/**
 * Fill the context menu and opens it.
 * @param data
 * @param handler
 */
function openMenu(data, handler) {
	var control = handler.canvas.id + '_contextMenu';
	var posx = handler.contextX + window.pageXOffset +'px';
	var posy = handler.contextY + window.pageYOffset + 'px';
	document.getElementById(control).style.position = 'absolute';
	document.getElementById(control).style.display = 'inline';
	document.getElementById(control).style.left = posx;
	document.getElementById(control).style.top = posy; 
	var list = handler.hexagonField.getContextMenu(new Point(data.fieldX, data.fieldY));

	var table = document.getElementById(handler.canvas.id + '_contextMenuTable');
	
	for(var i = 0; i < list.length; i++) {
		var rows = table.rows.length - 1;
		var row = table.insertRow(i);
		var cell1 = row.insertCell(0);
		var element1 = document.createElement("div");
		element1.id = list[i].text;
		element1.innerHTML = list[i].text;
		element1.onclick = function(e) {cmCallback(e);};
		cell1.appendChild(element1);
	}
}

/**
 * Hides the context menu.
 * @param control
 * @param id
 */
function HideMenu(control, id) {
	document.getElementById(id + '_contextMenu').style.display = 'none'; 
	var table = document.getElementById(id + '_contextMenuTable');
	var rows = table.rows.length;
	for(var i =rows - 1; i >= 0; i--) {
		table.deleteRow(i);
	}
}

function cmCallback(i) {
	console.log(i.target.id);
}