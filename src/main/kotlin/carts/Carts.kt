package carts

import java.util.ArrayDeque
import java.util.Deque

fun parseIntoMap(rawData: List<String>): Array<CharArray> = Array(rawData.size) { yIndex ->
    CharArray(rawData[yIndex].length) { xIndex ->
        rawData[yIndex][xIndex]
    }
}

fun findCarts(initialMap: Array<CharArray>): List<Cart> {

    tailrec fun collectCartsFromRow(row: CharArray, yIndex: Int, xIndex: Int, carts: List<Cart>): List<Cart> =
        if (xIndex == row.size) carts
        else {
            val newCarts = row[xIndex]
                .takeIf { Direction.values().map { it.mapMark }.contains(it) }
                ?.let { Cart(y = yIndex, x = xIndex, direction = it, map = initialMap) }
                ?.let { carts + it }
                ?: carts

            collectCartsFromRow(row, yIndex, xIndex + 1, newCarts)
        }

    tailrec fun collectCarts(yIndex: Int, carts: List<Cart>): List<Cart> =
        if (yIndex == initialMap.size) carts
        else {
            val newCarts = collectCartsFromRow(initialMap[yIndex], yIndex, 0, carts)
            collectCarts(yIndex + 1, newCarts)
        }

    return collectCarts(0, emptyList())
}

fun run(rawData: List<String>): Pair<Int, Int> {
    val map = parseIntoMap(rawData)
    val carts = findCarts(map)

    tailrec fun moveCartsAndReport(cartsQueue: Deque<Cart>): Cart? {
        if (cartsQueue.isEmpty()) return null
        val currentCart = cartsQueue.pop()
        currentCart.move()
        return if (currentCart.isCrashed) currentCart
        else moveCartsAndReport(cartsQueue)
    }

    tailrec fun runCycle(index: Int): Cart {
        val sortedCarts = carts.sortedWith(Comparator { o1, o2 ->
            (o1.positionY - o2.positionY).takeUnless { it == 0 } ?: (o1.positionX - o2.positionX)
        })

//        printMap(map)

        return moveCartsAndReport(ArrayDeque(sortedCarts)) ?: runCycle(index + 1)
    }

    return runCycle(0).let { it.positionX to it.positionY }.also { printMap(map) }
}

fun printMap(map: Array<CharArray>){
    for(y in 0 until map.size){
        for (x in 0 until map[y].size){
            if(x == map[y].lastIndex) println(map[y][x])
            else print(map[y][x])
        }
    }
}

