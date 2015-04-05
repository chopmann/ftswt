/**
 * @param columnSize
 * @param hexagonSideSize
 * @param mapType
 * @constructor
 */
Board = function(columnSize, hexagonSideSize, mapType) {
    this.map = null;
    this.hexagonSideSize = hexagonSideSize;
    this.columnSize = columnSize;
    this.mapType = mapType;
    switch (mapType) {
        case 'oddRowMap':
            this.map = oddRowMap(columnSize, hexagonSideSize);
            break;
        default :
            this.map = oddRowMap(columnSize, hexagonSideSize);
            break
    }
};

oddRowMap = function(columnSize, hexagonSideSize) {
    var map = {};
    var track = 0;
    var hex_count = 0;
    for (var z = 0; z < columnSize; z++) {
        var res = z % 2;
        var x_count = columnSize - (res);
        var x = 0;
        if (res == 0 && z > 0) {
            track++;
            x = x - track;
            x_count = x_count - track;
        }
        if (res == 1) {
            x = 0 - track;
            x_count = x_count - track;
        }
        for (;x < x_count; x++) {
            var y  = -x-z;
            var coordinates = new Cube(x, y, z);
            if (Math.floor(Math.random() * 2) == 0) {
                var hexagon = new Hexagon(coordinates, hexagonSideSize);
                hex_count++;
                map[coordinates]= hexagon;
            }
        }
    }
    console.log("Hex Count: " + hex_count);
    return map
};

/**
 * Draws the Hexagon into the given context.
 *
 * @param {CanvasRenderingContext2D} ctx - Canvas 2d context.
 * @param hex
 */
drawMap = function (ctx, map) {
    drawHexagonGrid(ctx, map);
    // drawForeground(ctx);
};

drawHexagonGrid= function(ctx, map) {
    for(var coordinate_hexagon in map) {
        drawHexagon(ctx, map[coordinate_hexagon]);
    }
};

boardClickListener = function(e, board) {
    console.log('click_offset: ' + e.offsetX + '/' + e.offsetY);
    var size = board.hexagonSideSize;
    var height = size  * 2;
    var width = Math.sqrt(3)/ 2 * height ;
    var q = e.offsetX/width -1/2;
    var r = (4 * (e.offsetY - size) ) / (3 * height ) ;
    var click_point = new Axial(e.offsetX, e.offsetY)
    var click_cube = (new Axial(q,r)).toCubefromOffset_OddR();
    var first_candidate_coord = cube_round(click_cube);
    var neighbors = hex_neighbors(first_candidate_coord);
    var candidates = [first_candidate_coord].concat(neighbors);
    var resp = null;
    for (var i = 0; i<candidates.length; i++) {
        var hex = board.map[candidates[i]];
        if(typeof hex !== 'undefined') {
            if (hex.isPointIn(click_point)) {
                console.log("It's a hit!");
                console.log('Candidate Nr: ' + i);
                console.log((candidates[i]));
                resp = hex;
                break;
            }
        }
    }
    if (resp == null) {
        console.log("No hit!");
    }
};