package power.grid

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

data class Cell(val x: Int, val y: Int, val serialNumber: Int) {

    val rackId = x + 10

    val powerLevel by lazy { ((((rackId * y + serialNumber) * rackId) / 100).toString().last() + "").toInt() - 5 }

    fun getGrid(allCells: Array<IntArray>, size: Int): Grid? {

        if(x + size > allCells.size || y + size > allCells[0].size) return null

        val grid = ArrayList<Int>(size * size)

        for(loopX in 0 until size){
            for (loopY in 0 until size){
                val cell = allCells[x + loopX][y + loopY]

                cell.let { grid.add(it) }
            }
        }

        return grid
            .takeIf { it.size == size * size }
            ?.let { power ->
                Grid(x = x, y = y, powerLevel = power.sum(), size = size)
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