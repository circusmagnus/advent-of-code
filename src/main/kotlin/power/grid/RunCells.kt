package power.grid

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.maxBy
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val SERIAL = 5153

fun createPowerCells(): Array<IntArray> = Array(301) { outerIndex ->
    IntArray(301) { innerIndex -> Cell(outerIndex, innerIndex, SERIAL).powerLevel }
}

//private fun CoroutineScope.produceCells(allCells: Array<Array<Cell>>): ReceiveChannel<Cell> = produce {
//    allCells.flatten().forEach { send(it) }
//    close()
//}
//
//private fun CoroutineScope.getGridsViaChannel(allCells: Array<Array<Cell>>, inputChannel: ReceiveChannel<Cell>, gridSize: Int): ReceiveChannel<Grid> {
//
//    val outPutChannel = Channel<Grid>()
//
//    repeat(4) {
//        launch {
//            for (cell in inputChannel) {
//                cell.getGrid(allCells, gridSize)?.let { grid -> outPutChannel.send(grid) }
//            }
//            outPutChannel.close()
//        }
//    }
//    return outPutChannel
//}

//private suspend fun getGridsAsync(allCells: Array<Array<Cell>>, gridSize: Int): Collection<Grid> = coroutineScope {
//    allCells.flatten().map { async { it.getGrid(allCells, gridSize) } }.mapNotNull { it.await() }
//}

private fun getGridsSync(allCells: Array<IntArray>, gridSize: Int): List<Grid> {
    val grids = mutableListOf<Grid>()
    for (x in 0 until allCells.size) {
        for (y in 0 until allCells[0].size) {
            Cell(x, y, SERIAL).getGrid(allCells, gridSize)?.let { grids.add(it) }
        }
    }

    return grids
}

fun getBestGrid(size: Int): Grid? {

    val allCells = createPowerCells()

    return getGridsSync(allCells, size).maxBy { it.powerLevel }
}

private suspend fun getBestGridsForSizesAsync(size: Int) = coroutineScope {
    val ongoingComputations = mutableListOf<Deferred<Grid?>>()
    for (x in 1..size) {
        ongoingComputations.add(async { getBestGrid(size) })
    }
    ongoingComputations.mapNotNull { it.await() }
}

fun getBestGridSize(): Grid? {

    return runBlocking {

        val fanOutChannel = Channel<Int>()
        val fanInChannel = Channel<Grid>()

        println("channels created")

        launch {
            for (x in 1..301) {
                println("send grid size for work: $x")
                fanOutChannel.send(x)
            }
            fanOutChannel.close()
        }

        repeat(Runtime.getRuntime().availableProcessors().also { println("available processors: $it") }) { worker ->
            launch(Dispatchers.Default) {
                for (gridSize in fanOutChannel) {
                    println("processing grid size: $gridSize started in worker $worker")
                    getBestGrid(gridSize).also { println("processing grid: $it for grid size: $gridSize done in worker: $worker") }?.let { fanInChannel.send(it) }
                }
                fanInChannel.close()
            }
        }
        fanInChannel.maxBy { it.powerLevel }
    }

//    return runBlocking {
//                 generateSequence (1){ size -> size + 1 }
//            .take(301)
//                    .map { it.also { println("processing grid size: $it started") } }
//                    .toList()
//                    .map { size -> async { getBestGrid(size) } }
//                    .mapNotNull { it.await().also { done -> println("processing grid size: $done done") } }
//                    .maxBy { it.powerLevel }
//    }
//        return generateSequence (1){ size -> size + 1 }
//            .take(301)
//            .map { it.also { println("processing grid size: $it") } }
//            .mapNotNull { size -> getBestGrid(size) }
//            .maxBy { it.powerLevel }
}



