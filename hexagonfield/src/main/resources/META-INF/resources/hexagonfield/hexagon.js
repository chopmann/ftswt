/**
 * Created by sirmonkey on 4/2/15.
 */


/*
 * ##############################################################################################
 * #										Hexagon												#
 * ##############################################################################################
 */

/**
 * Represent one hexagon.
 *
 * @constructor
 * @param {Cube} coordinate
 * @param {number} hexagonSideSize - Side length from the hexagon.
 */
function Hexagon(coordinate, hexagonSideSize) {
    this.coordinate = coordinate;
    this.center = hex_center(this.coordinate, hexagonSideSize);
    this.corners = hex_corners(this.center, hexagonSideSize);
    this.size = hexagonSideSize;
    this.bgImg = new Image();
    this.bgImg.src = "resources/images/grass.jpg";
    this.foregroundImg = null;
    this.bordersColor = ['blue','black','red','blue','black','red']
}
/*
 * ##############################################################################################
 * #										Point												#
 * ##############################################################################################
 */

/**
 * Axial Coordinate Point on cavas
 *
 * @constructor
 * @param q Column (x-axis on convas)
 * @param r Row (y-axis on canvas)
 */
Axial = function(q, r){
    this.q = q;
    this.r = r;
};

/**
 * Cube Coordinate
 *
 * @constructor
 * @param x
 * @param y
 * @param z
 */
Cube = function (x, y, z) {
    this.x = x;
    this.y = y;
    this.z = z;
};
/**
 * Converts cube coordinate into an axial coordinate.
 * @returns {Axial}
 */
Cube.prototype.toAxial = function() {
    return new Axial(this.x, this.y);
};

/**
 * Convert cube coordinate into an easy for canvas coordinates
 * @returns {Axial}
 */
Cube.prototype.toOffset_OddR = function () {
    var q = this.x + (this.z - (this.z&1)) / 2;
    var r = this.z;
    return new Axial(q, r);
};

/**
 * Calculates the center of a Hexagon in canvas representation.
 *
 * @constructor
 * @param {Cube} coordinate
 * @param {Number} size of the Hexagon.
 * @returns {Axial}
 */
hex_center = function(coordinate, size) {
    var hex = coordinate.toOffset_OddR();
    console.log(hex);
    var height = size  * 2;
    var width = Math.sqrt(3)/ 2 * height ;
    var x = (width / 2) + width*hex.q;
    var y = size + 3/4 * height * hex.r
    if(hex.r == 0) {
        x = (width / 2) + width*hex.q;
        y = size
    } else if(hex.r % 2 == 1){
        x = hex.q == 0 ? width:width*hex.q;
    }
    return new Axial(x, y);
};


/**
 *    0____1
 *   /      \
 * 5/   C    \2
 *  \       /
 *  4\ ___ /3
 * @param center
 * @param size
 * @param i
 * @param topped Orientation (pointy or flat)
 * @returns {Axial}
 */
hex_corner = function (center, size, i, topped) {
    topped = typeof topped !== 'undefined' ?  topped : 'flat';
    var adjust = topped !== 'pointy' ? 0 : 90;
    var angle_deg = 60 * i + adjust;
    var angle_rad = Math.PI / 180 * angle_deg;
    return new Axial(center.q + size * Math.cos(angle_rad),
        center.r + size * Math.sin(angle_rad));
};
/**
 *
 * @param center
 * @param size
 * @returns {Array} Containing the Corners
 */
hex_corners = function (center, size) {
    var corners = [];
    for(var i = 0; i < 6; i++) {
        corners.push(hex_corner(center,size, i, 'pointy'));
    }
    return corners;
};

/**
 * Checks if the given point is inside the field.
 * To calculate this the cross product is used with the third dimension set to zero.
 *
 * @param {Point} pt - Point to check.
 * @returns {boolean} true if the point is inside and false if not.
 */
Hexagon.prototype.isPointIn = function (pt) {
    var result = true;
    var i, j;
    for (i = 0, j = this.corners.length - 1; i < this.corners.length && result == true; j = i++) {
        if (0 < ((this.corners[j].x - this.corners[i].x) * (pt.y - this.corners[i].y) - (this.corners[j].y - this.corners[i].y) * (pt.x - this.corners[i].x)) && result != false) {
            result = false;
        }
    }
    return result;
};

/**
 * Draws the Hexagon into the given context.
 *
 * @param {CanvasRenderingContext2D} ctx - Canvas 2d context.
 * @param hex
 */
drawHexagon = function (ctx, hex) {
    drawHexagonBackground(ctx, hex);
    drawHexagonSides(ctx, hex);
   drawForeground(ctx, hex);
};

/**
 * Creates the hexagon path for drawing.
 *
 * @param {CanvasRenderingContext2D} ctx - Canvas 2d context.
 * @param first
 * @param second
 * @param begin
 * @param close
 */
setHexagonSide = function (ctx , first, second, begin , close ) {
    //TODO: Refactor
    begin = typeof begin !== 'undefined' ?  begin : true;
    close = typeof close !== 'undefined' ?  close : true;
    if (begin) {
        ctx.beginPath();
        ctx.moveTo(first.q, first.r);
    }
    ctx.lineTo(second.q, second.r);
    if (close) ctx.closePath();
};
drawHexagonSide = function (ctx , first, second, color) {
    ctx.lineWidth = 1;
    ctx.strokeStyle = color;
    setHexagonSide(ctx,first,second);
    ctx.stroke();
};

/**
 * Draws the border of the field.
 *
 * @param {CanvasRenderingContext2D} ctx - Canvas 2d context.
 * @param hex
 */
drawHexagonSides = function (ctx, hex) {
    //TODO: Refactor
    ctx.save();
    for (var i = 0; i < hex.bordersColor.length; i++) {
        var second = null;
        if (i + 1 < hex.bordersColor.length) {
            second = hex.corners[i+1];
        } else {
            second = hex.corners[0];
        }
        drawHexagonSide(ctx,hex.corners[i],second, hex.bordersColor[i])
    }
    ctx.restore();
};

/**
 *
 * @param ctx
 * @param hex
 */
setHexagonSides = function(ctx, hex) {
    //TODO: Refactor
    setHexagonSide(ctx, hex.corners[0], hex.corners[1], true, false);
    setHexagonSide(ctx, hex.corners[1], hex.corners[2], false, false);
    setHexagonSide(ctx, hex.corners[2], hex.corners[3], false, false);
    setHexagonSide(ctx, hex.corners[3], hex.corners[4], false, false);
    setHexagonSide(ctx, hex.corners[4], hex.corners[5], false, false);
    setHexagonSide(ctx, hex.corners[5], hex.corners[0], false, true);

};

/**
 * Draws the field background.
 *
 * @param {CanvasRenderingContext2D} ctx - Canvas 2d context.
 * @param hex
 */
drawHexagonBackground = function (ctx, hex) {
    if (hex.bgImg != null) {
        ctx.save();
        setHexagonSides(ctx, hex);
        ctx.clip();
        ctx.drawImage(hex.bgImg, 0, 0, hex.bgImg.width, hex.bgImg.height, hex.corners[5].q, hex.corners[0].r, hex.corners[2].q - hex.corners[5].q, hex.corners[3].r - hex.corners[0].r);
        ctx.restore();
    }
};

drawForeground = function (ctx, hex) {
    ctx.save();
    ctx.font="10px Georgia";
    ctx.strokeStyle = "white";
    var tmp = hex.coordinate.toOffset_OddR();
    ctx.fillText(tmp.r+"/"+tmp.q, hex.center.q - 5, hex.center.r - 5);
    ctx.restore();
};

