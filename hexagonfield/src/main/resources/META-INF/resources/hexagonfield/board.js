/**
 * @param columnSize
 * @param hexagonSideSize
 * @param mapType
 * @constructor
 */
Board = function(columnSize, hexagonSideSize, mapType) {
    this.map = null;
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
    var map = [];
    var track = 0;
    for (var z = 0; z < columnSize; z++) {
        map.push([]);
        x = q - (r - (r&1)) / 2
        z = r
        y = -x-z
        var res = z % 2;
        var x_count = columnSize - (res);
        var x = 0;
        if (res == 0 && z !== 0) {
            track++;
            x = x - track;
            x_count = x_count - track;
        }
        for (;x < x_count; x++) {
            var y  = -x-z;
            var hexagon = new Hexagon(new Cube(x, y, z), hexagonSideSize);
            map[z].push(hexagon);
            console.log(hexagon)
        }
    }
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
    for(var z = 0; z<map.length; z++) {
        for(var x = 0; x <map[z].length; x++) {
            drawHexagon(ctx, map[z][x]);
        }
    }
}