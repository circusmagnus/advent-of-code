package power.grid

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

data class Cell(val x: Int, val y: Int, val serialNumber: Int) {

    val rackId = x + 10

    val powerLevel by lazy { ((((rackId * y + serialNumber) * rackId) / 100).toString().last() + "").toInt() - 5 }

    fun getGrid(allCells: Array<Array<Cell>>, size: Int): Grid? {

        val grid = ArrayList<Cell>(size * size)

        for(loopX in 1..size){
            for (loopY in 1..size){
                val cell = try {
                    allCells[x + loopX][y + loopY]
                } catch (e: ArrayIndexOutOfBoundsException){
                    null
                }
                cell?.let { grid.add(it) }
            }
        }

        return grid
            .takeIf { it.size == size * size }
            ?.sortedBy { it.x + it.y }
            ?.let { cells ->
                Grid(x = cells.first().x, y = cells.first().y, powerLevel = cells.map { it.powerLevel }.sum(), size = size)
            }
    }



//            = allCells
//        .asSequence()
//        .filter { (it.x - this.x) in -1..1 }
//        .filter { (it.y - this.y) in -1..1 }
//        .takeIf { it.count() == 9 }
//        ?.sortedBy { it.x + it.y }
//        ?.let { cells ->
//            Grid(x = cells.first().x, y = cells.first().y, powerLevel = cells.map { it.powerLevel }.sum())
//        }

}

data class Grid(val x: Int, val y: Int, val powerLevel: Int, val size: Int)