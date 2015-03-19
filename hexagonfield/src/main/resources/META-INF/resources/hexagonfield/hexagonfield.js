/*
 * ##############################################################################################
 * #										Point												#
 * ##############################################################################################
 */

/**
 * Simple point object.
 * 
 * @constructor
 * @param {number} x - X position.
 * @param {number} y - Y position.
 */
function Point(x, y) {
	this.x = x;
	this.y = y;
}

/*
 * ##############################################################################################
 * #										Hexagon												#
 * ##############################################################################################
 */

/**
 * Constructor for an field.
 * Represent one hexagon field.
 * 
 * @constructor
 * @param {number} x - X position.
 * @param {number} y - y position.
 * @param {number} size - Side length from the hexagon.
 * @param {Image} bgImg - Background image for the field.
 */
function Hexagon(x, y, size, bgImg) {
	this.x = x;
	this.y = y;
	this.size = size;
	this.bgImg = bgImg;
    this.foregroundImg = null;
}

/**
 * Calculate the 6 points from the hexagon.
 */
Hexagon.prototype.calcPoints = function() {
	this.h = Math.sqrt(3) * this.size / 2;
	this.points = [
		new Point(this.x, this.y),
		new Point(this.x + this.size, this.y),
		new Point(this.x + 1.5 * this.size, this.y + this.h),
		new Point(this.x + this.size, this.y + 2 * this.h),
		new Point(this.x, this.y + 2 * this.h),
		new Point(this.x - 0.5 * this.size, this.y + this.h)
	];
};

/**
 * Checks if the given point is inside the field.
 * To calculate this the cross product is used with the third dimension set to zero.
 * 
 * @param {Point} pt - Point to check.
 * @returns {boolean} true if the point is inside and false if not.
 */
Hexagon.prototype.isPointIn = function(pt) {
	var result = true;
	var i, j;
	for(i = 0, j = this.points.length - 1; i < this.points.length && result == true; j = i++) {
		if(0 < ((this.points[j].x - this.points[i].x) * (pt.y - this.points[i].y) - (this.points[j].y - this.points[i].y) * (pt.x - this.points[i].x)) && result != false) {
			result = false;
		}
	}
	return result;
};

/**
 * Draws the complete field into the given context.
 * 
 * @param {CanvasRenderingContext2D} ctx - Canvas 2d context.
 */
Hexagon.prototype.draw = function(ctx) {
	this.drawBackground(ctx);
	this.drawBorder(ctx);
    this.drawForeground(ctx);
};

/**
 * Creates the hexagon path for drawing.
 * 
 * @param {CanvasRenderingContext2D} ctx - Canvas 2d context.
 */
Hexagon.prototype.drawPath = function(ctx) {
	ctx.beginPath();
    ctx.moveTo(this.points[0].x, this.points[0].y);
    ctx.lineTo(this.points[1].x, this.points[1].y);
    ctx.lineTo(this.points[2].x, this.points[2].y);
    ctx.lineTo(this.points[3].x, this.points[3].y);
    ctx.lineTo(this.points[4].x, this.points[4].y);
    ctx.lineTo(this.points[5].x, this.points[5].y);
    ctx.lineTo(this.points[0].x, this.points[0].y);
	ctx.closePath();
};

/**
 * Draws the border of the field.
 * 
 * @param {CanvasRenderingContext2D} ctx - Canvas 2d context.
 */
Hexagon.prototype.drawBorder = function(ctx) {
	ctx.save();
    this.drawPath(ctx);
	ctx.stroke();
	ctx.restore();
};

/**
 * Draws the field background.
 * 
 * @param {CanvasRenderingContext2D} ctx - Canvas 2d context.
 */
Hexagon.prototype.drawBackground = function(ctx) {
	if(this.bgImg != null) {
		ctx.save();
		this.drawPath(ctx);
		ctx.clip();
		ctx.drawImage(this.bgImg, 0, 0, this.bgImg.width, this.bgImg.height, this.points[5].x, this.points[0].y, this.points[2].x - this.points[5].x, this.points[3].y - this.points[0].y);
		ctx.restore();
	}
    this.drawForeground(ctx);
};

Hexagon.prototype.drawForeground = function(ctx) {
    if(this.foregroundImg != null) {
        ctx.save();
        this.drawPath(ctx);
        ctx.clip();
        ctx.drawImage(this.foregroundImg, 0, 0, this.foregroundImg.width, this.foregroundImg.height, this.points[5].x, this.points[0].y, this.points[2].x - this.points[5].x, this.points[3].y - this.points[0].y);
        ctx.restore();
    }
};

Hexagon.prototype.setContext = function(context) {
	console.log(context);
	this.context = context;
}

Hexagon.prototype.getContext = function() {
	return this.context;
}

/*
 * ##############################################################################################
 * #									HexagonField											#
 * ##############################################################################################
 */

/**
 * Constructor for the HexagonField.
 * Represents the complete fields.
 * 
 * @constructor
 * @param {number} width - Canvas width.
 * @param {number} height - Canvas height.
 * @param {HTMLCanvasElement} canvas - The Canvas.
 */
function HexagonField(width, height, canvas) {
	// canvas width
	this.width = width;
	// canvas height
	this.height = height;
	// The canvas which is for the component
	this.canvas = canvas;
	// Context to draw in
	this.ctx = canvas.getContext('2d');
	// Field count horizontal
	this.countX = 0;
	// Field count vertical
	this.countY = 0;
	// Field side length
	this.size = 0;
	// Field array
	this.hexagons = [];
	// Point where mouse button down
	this.mouseDown = new Point(0, 0);
	// Point where mouse button up
	this.mouseUp = new Point(0, 0);
	// Selected fields
	this.selectedFields = [];

    this.highlightCenter = new Point(-1, -1);
    this.highlightedFields = [];

    this.bgimg;
    //this.bgimg.src = "resources/images/stars.jpg";

    this.fgimg;
    //this.fgimg.src = "resources/images/clouds.png";
}

/**
 * Initialize the field.
 */
HexagonField.prototype.init = function() {
	this.canvas.width = this.width;
	this.canvas.height = this.height;
	
	// Drawing start points
	this.offsetX = this.size / 2;
	this.offsetY = 0;
	// Height from the internal triangle of an hexagon
	this.h = Math.sqrt(3) * this.size / 2;
	
	this.drawLoading();
	
	// Create the array of hexagons
	for(var i = 0; i < this.countY; i++) {
		this.hexagons.push([]); // create new array
		for(var j = 0; j < this.countX; j++) {
			this.hexagons[i].push(new Hexagon(this.offsetX + 1.5 * this.size * j, this.offsetY + 2 * this.h * i + j%2 * this.h, this.size, null));
		}
	}

	this.visibleFields();
	this.visibleFieldsCalc();
	
	this.canvas.addEventListener('click', this.onClick.bind(this), false);
	this.canvas.addEventListener('mousedown', this.mousedown.bind(this), false);
	this.canvas.addEventListener('mouseup', this.mouseup.bind(this), false);
	this.canvas.addEventListener('mousemove', this.mousemove.bind(this), false);
	this.canvas.addEventListener('mouseout', this.mouseup.bind(this), false);
	
	/* Touchscreen Testing*/
	this.canvas.addEventListener("touchstart", function(event) {
		this.scrollable = true;
		this.mouseDown.x = event.targetTouches[0].pageX;
		this.mouseDown.y = event.targetTouches[0].pageY;
	}.bind(this), true);
	
	this.canvas.addEventListener("touchend", function(event) {
		this.scrollable = false;
		this.mouseUp.x = event.targetTouches[0].pageX;
		this.mouseUp.y = event.targetTouches[0].pageY;
	}.bind(this), true);
	
	this.canvas.addEventListener("touchmove", function(event) {
		if(this.scrollable) {
			if(this.mouseDown.x < event.targetTouches[0].pageX || this.mouseDown.x > event.targetTouches[0].pageX
				|| this.mouseDown.y < event.targetTouches[0].pageY || this.mouseDown.y > event.targetTouches[0].pageY) {
				this.scrolled = true;
				this.mouseUp.x = event.targetTouches[0].pageX;
				this.mouseUp.y = event.targetTouches[0].pageY;
				this.scroll();
			}
		}
	}.bind(this), true);
	/* Touchscreen Testing*/
};

/**
 * Removes the field from the selected list.
 * @param {Hexagon || Point} field - Field to remove.
 */
HexagonField.prototype.removeSelection = function(field) {
    var x = field.x;
    var y = field.y;
    for (var i = 0; i < this.selectedFields.length; i++) {
        if (this.selectedFields[i].x == x && this.selectedFields[i].y == y) {
            this.selectedFields.splice(i, 1);
            this.draw();
        }
    }
};

HexagonField.prototype.setContextMenu = function(field, context) {
	this.hexagons[field.x][field.y].setContext(context);
}

HexagonField.prototype.getContextMenu = function(field) {
	return this.hexagons[field.x][field.y].getContext();
}

/*
 * ##############################################################################################
 * #										Visibility											#
 * ##############################################################################################
 */

/**
 * Calculates the new offset while scrolling the map around
 * after that the canvas will be redrawn and the mouse up position 
 * will be the new mouse down.
 */
HexagonField.prototype.scroll = function() {
	this.offsetX += this.mouseUp.x - this.mouseDown.x;
	this.offsetY += this.mouseUp.y - this.mouseDown.y;
	this.mouseDown.x = this.mouseUp.x;
	this.mouseDown.y = this.mouseUp.y;
	
	this.visibleFields();
	this.visibleFieldsCalc();
	
	this.draw();
};

/**
 * Calculates which fields from the array are visible in the canvas.
 */
HexagonField.prototype.visibleFields = function() {
	var startX = 0 - (parseInt(this.offsetX / (1.5 * this.size)));
	var startY = 0 - (parseInt(this.offsetY / (2 * this.h))) - 1;
	if(startX < 0) {
		startX = 0;
	}
	if(startY < 0) {
		startY = 0;
	}
	this.visibleStart = new Point(startX, startY);
	var endX = startX + parseInt(this.width / (1.5 * this.size)) + 2;
	var endY = startY + parseInt(this.height / (2 * this.h) + 3);
	this.visibleEnd = new Point(endX, endY);
};

/**
 * Calculates for the visible fields the points where they are drawed.
 */
HexagonField.prototype.visibleFieldsCalc = function() {
	for(var i = this.visibleStart.y; i < this.countY && i < this.visibleEnd.y; i++) {
		for(var j = this.visibleStart.x; j < this.countX && j < this.visibleEnd.x; j++) {
			this.hexagons[i][j].x = this.offsetX + 1.5 * this.size * j;
			this.hexagons[i][j].y = this.offsetY + 2 * this.h * i + j%2 * this.h;
			this.hexagons[i][j].calcPoints();
		}
	}
};

/*
 * ##############################################################################################
 * #										Drawing												#
 * ##############################################################################################
 */

/**
 * Draws the loading information in the middle of the canvas.
 */
HexagonField.prototype.drawLoading = function() {
	this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
	
	var x = this.width / 2;
	var y = this.height / 2;

	this.ctx.font = '30pt Calibri';
	this.ctx.textAlign = 'center';
	this.ctx.fillStyle = 'black';
	this.ctx.fillText('Loading...', x, y);
};

/**
 * Redraws the complete canvas.
 * First draws an empty rectangle to clear the old state and
 * then draws the fields which are in the visible area.
 */
HexagonField.prototype.draw = function() {
    this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

//    this.ctx.fillStyle="#FF0000";
//    this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
    if (this.bgimg) {
        this.ctx.save();
        this.ctx.drawImage(this.bgimg, 0, 0, this.canvas.width, this.canvas.height);
        this.ctx.restore();
    }


	for(var i = this.visibleStart.y; i < this.countY && i < this.visibleEnd.y; i++) {
		for(var j = this.visibleStart.x; j < this.countX && j < this.visibleEnd.x; j++) {
			this.hexagons[i][j].draw(this.ctx);
		}
	}

    this.drawHighlight();
	this.drawSelected();

//    this.ctx.save();
//    this.ctx.drawImage(this.fgimg, 0, 0, this.canvas.width, this.canvas.height);
//    this.ctx.restore();
};

/**
 * Draws an other border around the visible and selected fields.
 */
HexagonField.prototype.drawSelected = function() {
	for(var i = 0; i < this.selectedFields.length; i++) {
		if(this.selectedFields[i].x >= this.visibleStart.x && this.selectedFields[i].x <= this.visibleEnd.x 
			&& this.selectedFields[i].y >= this.visibleStart.y && this.selectedFields[i].y <= this.visibleEnd.y) {
			this.ctx.save();
			this.ctx.strokeStyle = 'red';
			this.ctx.lineWidth = 5;
			this.hexagons[this.selectedFields[i].y][this.selectedFields[i].x].drawBorder(this.ctx);
			this.ctx.restore();
		}
	}
};

/**
 * Draws the highlight on the visible fields that are in the highlightedFields list.
 */
HexagonField.prototype.drawHighlight = function() {

    this.highlightedFields.forEach(function(point) {
        if (point.x >= this.visibleStart.x && point.x < this.visibleEnd.x && point.y >= this.visibleStart.y && point.y < this.visibleEnd.y) {
            this.ctx.save();
            this.ctx.fillStyle = 'rgba(255,255,255,0.5)';
            this.hexagons[point.y][point.x].drawPath(this.ctx);
            this.ctx.fill();
            this.ctx.stroke();
            this.ctx.restore();
        }
    }.bind(this));
};

/*
 * ##############################################################################################
 * #									Mouse functions											#
 * ##############################################################################################
 */

/**
 * On click listener which search from the visible fields the one which was clicked
 * and add it to the selected list.
 * 
 * @param {Event} event - Mouse event
 */
HexagonField.prototype.onClick = function(event) {
	if(this.mouseDown.x == this.mouseUp.x && this.mouseDown.y == this.mouseUp.y && !this.scrolled) {
		var found = false;
		for(var i = this.visibleStart.y; i < this.hexagons.length && i < this.visibleEnd.y && !found; i++) {
			for(var j = this.visibleStart.x; j < this.hexagons[i].length && j < this.visibleEnd.x && !found; j++) {
				if(this.hexagons[i][j].isPointIn(new Point(event.layerX, event.layerY))) {
                    var alreadyInList = this.selectedFields.contains(new Point(j, i));
                    if (!alreadyInList) {
                        this.selectedFields.push(new Point(j, i));
                        this.drawSelected();
                        this.hexagons[i][j].drawForeground(this.ctx);
                    }
                    found = true;
                    this.fieldClicked(j, i);
				}
			}
		}
	} else {
		this.scrolled = false;
	}
};

/**
 * On mouse down activate the ability to scroll and save down position.
 * 
 * @param {Event} event - Mouse event
 */
HexagonField.prototype.mousedown = function(event) {
	this.scrollable = true;
	this.mouseDown.x = event.layerX;
	this.mouseDown.y = event.layerY;
	HideMenu('contextMenu', this.canvas.id);
};

/**
 * On mouse up deactivate the ability to scroll and save up position.
 * 
 * @param {Event} event - Mouse event
 */
HexagonField.prototype.mouseup = function(event) {
	this.scrollable = false;
	this.mouseUp.x = event.layerX;
	this.mouseUp.y = event.layerY;
};

/**
 * On mouse move check if the mouse is down (scrollable)
 * then set the mouse up position and call scroll.
 * 
 * @param {Event} event - Mouse event
 */
HexagonField.prototype.mousemove = function(event) {
	if(this.scrollable) {
		if(this.mouseDown.x < event.layerX || this.mouseDown.x > event.layerX
			|| this.mouseDown.y < event.layerY || this.mouseDown.y > event.layerY) {
			this.scrolled = true;
			this.mouseUp.x = event.layerX;
			this.mouseUp.y = event.layerY;
			this.scroll();
		}
	}
};

/*
 * ##############################################################################################
 * #       									Misc    										    #
 * ##############################################################################################
 */

/**
 * Calculates which fields are in range to reach.
 * @param {number} range - The range around the center field.
 * @param {Point} highlightCenter - Center for highlighting.
 */
HexagonField.prototype.setHighlight = function(range, highlightCenter) {
    this.highlightCenter = highlightCenter;
    if(this.highlightCenter.x >= 0 && this.highlightCenter.x < this.hexagons[0].length && this.highlightCenter.y >= 0 && this.highlightCenter.y < this.hexagons.length) {
        var startx = this.highlightCenter.x - range;
        var starty = this.highlightCenter.y - range;
        var endx = this.highlightCenter.x + range;
        var endy = this.highlightCenter.y + range;
        var deltaX;
        var deltaY;

        for(var y = starty; y <= endy; y++) {
            for(var x = startx; x <= endx; x++) {

                deltaX = x - this.highlightCenter.x;
                deltaY = (Math.ceil(x /2) + y) - (Math.ceil(this.highlightCenter.x /2) + this.highlightCenter.y);
                if(((Math.abs(deltaX) + Math.abs(deltaY) + Math.abs(deltaX - deltaY)) / 2) <= range) {
                    this.highlightedFields.push(new Point(x, y));
                }
            }
        }
    }
};

/**
 *
 * @param {Hexagon || Point} obj
 * @returns {boolean} - True if contains else False
 */
Array.prototype.contains = function(obj) {
    var x = obj.x;
    var y = obj.y;
    for (var i = 0; i < this.length; i++) {
        if (this[i].x == x && this[i].y == y) {
            return true;
        }
    }
    return false;
};
