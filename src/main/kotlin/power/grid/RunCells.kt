package power.grid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.maxBy
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun createPowerCells(): Array<Array<Cell>> = Array(301) { outerIndex ->
    Array(301) { innerIndex ->
        Cell(x = outerIndex, y = innerIndex, serialNumber = 5153)
    }
}

private fun CoroutineScope.produceCells(allCells: Array<Array<Cell>>): ReceiveChannel<Cell> = produce {
    allCells.flatten().forEach { send(it) }
    close()
}

private fun CoroutineScope.getGridsViaChannel(allCells: Array<Array<Cell>>, inputChannel: ReceiveChannel<Cell>, gridSize: Int): ReceiveChannel<Grid> {

    val outPutChannel = Channel<Grid>()

    repeat(4) {
        launch {
            for (cell in inputChannel) {
                cell.getGrid(allCells, gridSize)?.let { grid -> outPutChannel.send(grid) }
            }
            outPutChannel.close()
        }
    }
    return outPutChannel
}

private suspend fun getGridsAsync(allCells: Array<Array<Cell>>, gridSize: Int): Collection<Grid> = coroutineScope {
    allCells.flatten().map { async { it.getGrid(allCells, gridSize) } }.mapNotNull { it.await() }
}

private fun getGridsSync(allCells: Array<Array<Cell>>, gridSize: Int) = allCells
    .flatten()
    .mapNotNull { it.getGrid(allCells, gridSize) }

fun getBestGrid(size: Int): Grid? {

    val allCells = createPowerCells()

   return getGridsSync(allCells, size).maxBy { it.powerLevel }
}

private suspend fun getBestGridsForSizesAsync(size: Int) = coroutineScope {
    val ongoingComputations = mutableListOf<Deferred<Grid?>>()
    for (x in 1..size){
        ongoingComputations.add(async { getBestGrid(size) })
    }
    ongoingComputations.mapNotNull { it.await() }
}

fun getBestGridSize(): Grid? {

//    return runBlocking {
//        getBestGridsForSizesAsync(6).maxBy { it.powerLevel }
//    }
        return generateSequence (1){ size -> size + 1 }
            .take(301)
            .mapNotNull { size -> getBestGrid(size) }
            .maxBy { it.powerLevel }
}



