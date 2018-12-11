package power.grid

//const val SERIAL = 5153
//
//fun createPowerCells2(size: Int): Array<Array<IntArray>> = Array(size + 1) { outerIndex ->
//    Array(size + 1) { innerIndex ->
//        IntArray(size + 1)
//    }
//}
//
//fun populateZeroLevelPower(allCells: Array<Array<IntArray>>): Array<Array<IntArray>> {
//
//
//    for(loopX in 1 until allCells.size){
//        for (loopY in 1 until allCells[0].size){
//            val cell = Cell(loopX, loopY, SERIAL)
//
//                val powerAtLevel = cell.getPowerForLevel(allCells, 0) ?: Int.MIN_VALUE
//                allCells[loopX][loopY][0] = powerAtLevel
//        }
//    }
//
//    return allCells
//}
//
//data class Result(val x: Int, val y: Int, val gridSize: Int, val power: Int)
//
//fun findHighestPowerLevel(size: Int, maxGridSize: Int): Result{
//    val powerCells = createPowerCells2(size)
//    val cellsWithBaseValue = populateZeroLevelPower(powerCells)
//
//    var highestPower: Result = Result(-1, -1, -1, Int.MIN_VALUE)
//
//    for(loopX in 1 until cellsWithBaseValue.size){
//        for (loopY in 1 until cellsWithBaseValue[0].size){
//            val cell = Cell(loopX, loopY, SERIAL)
//            for (powerLevel in 1 until maxGridSize){
//                val powerAtLevel = cell.getPowerForLevel(cellsWithBaseValue, powerLevel) ?: Int.MIN_VALUE
//                cellsWithBaseValue[loopX][loopY][powerLevel] = powerAtLevel
//                if (powerAtLevel > highestPower.power) {
//                    highestPower = Result(loopX, loopY, powerLevel, powerAtLevel)
//                }
//            }
//        }
//    }
//
//    return highestPower
//}