package signs

import java.util.Deque

class Map(private val startingPoints: Collection<Point>) {

    private tailrec fun drawRows(rows: Array<CharArray>, index: Int) {
        rows[index].let { drawPositionsInRow(it, 0) }

        if (index == rows.lastIndex) return
        else drawRows(rows, index + 1)
    }

    private tailrec fun drawPositionsInRow(positions: CharArray, index: Int) {
        positions[index].let { point ->
            if (index == positions.lastIndex) println(point)
            else print(point)
        }

        if (index == positions.lastIndex) return
        else drawPositionsInRow(positions, index + 1)
    }

    private fun drawMap() {
        val map = initMap()
        val (adjustX, adjustY) = adjustIndexesBy()
        startingPoints.forEach { map[it.positionY + adjustY][it.positionX + adjustX] = '#' }
        drawRows(map, 0)
    }

    private fun advanceTime() {
        startingPoints.forEach { it.updatePosition() }
    }

    tailrec fun watchForSign(howLong: Int, index: Int) {
        val arePointsAligned = startingPoints
            .none { it.hasNeighbours(startingPoints).not() }

        if (arePointsAligned) {
            println("pooints are aligned")
            drawMap()
            println("would Wait for $index")
        }
        else if (index == howLong) {
            println("cant wait anymore")
            drawMap()
        }
        else {
            advanceTime()
            watchForSign(howLong, index + 1)
        }
    }

    private data class MapArea(
        val minX: Int,
        val minY: Int,
        val maxX: Int,
        val maxY: Int
    )

    private fun getMapArea() = MapArea(
        minX = startingPoints.sortedBy { it.positionX }.first().positionX,
        minY = startingPoints.sortedBy { it.positionY }.first().positionY,
        maxX = startingPoints.maxBy { it.positionX }?.positionX ?: 0,
        maxY = startingPoints.maxBy { it.positionY }?.positionY ?: 0
    )

    private fun initMap(): Array<CharArray> {

        val (minX, minY, maxX, maxY) = getMapArea()

        return Array(maxY - minY + 1) {
            CharArray(maxX - minX + 1) { '.' }
        }
    }

    private data class IndexAdjust(val x: Int, val y: Int)

    private fun adjustIndexesBy(): IndexAdjust {
        val mapArea = getMapArea()
        return IndexAdjust(0 - mapArea.minX, 0 - mapArea.minY)
    }
}