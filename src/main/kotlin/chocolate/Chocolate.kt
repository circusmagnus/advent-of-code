package chocolate

data class Positions(val firstElf: Int, val secondElf: Int)

val startPositions = Positions(0, 1)
val startRecipies = "37"

fun run(magicNumber: String): Int {

    //    tailrec fun cycle(positions: Positions, recipes: String): String {
//        if (recipes.length >= magicNumber + 10) return recipes.drop(magicNumber).take(10)
//        val (firstElf, secondElf) = positions
//        val sumToAdd = recipes[firstElf].toString().toInt() + recipes[secondElf].toString().toInt()
//        val newString = recipes + sumToAdd
//        val newPositions = Positions(
//            firstElf = getNewPosition(firstElf, newString),
//            secondElf = getNewPosition(secondElf, newString)
//        )
//
////        println("after cycle: recipies: $recipes, elves: $positions")
//        return cycle(newPositions, newString)
//    }

//    tailrec fun cycle2(positions: Positions, recipes: String): String {
//        if (recipes.takeLast(7).contains(magicNumber)) return recipes
//            .replace(magicNumber, "s")
//            .takeWhile { it != 's' }
//
//        val (firstElf, secondElf) = positions
//        val sumToAdd = recipes[firstElf].toString().toInt() + recipes[secondElf].toString().toInt()
//        val newString = recipes + sumToAdd
//        val newPositions = Positions(
//            firstElf = getNewPosition(firstElf, newString),
//            secondElf = getNewPosition(secondElf, newString)
//        )
//
////        println("after cycle: recipies: $recipes, elves: $positions")
//        return cycle2(newPositions, newString)
//    }

//    return cycle2(startPositions, startRecipies).count()
    return TODO()
}

fun getNewPosition(positionIndex: Int, recipes: StringBuilder): Int {
    val moveDistance = 1 + (recipes[positionIndex] - '0')
    return (moveDistance + positionIndex) % recipes.length
}

fun run2(magicNumber: String): Int {

val startingRecipes = StringBuilder("37")

    tailrec fun cycle2(positions: Positions, magicList: StringBuilder): String {
        if (magicList.takeLast(magicNumber.length + 1).contains(magicNumber)) return magicList
            .toString()
            .replace(magicNumber, "s")
            .takeWhile { it != 's' }

        val (firstElf, secondElf) = positions
        val sumToAdd = ((magicList[firstElf] - '0') + (magicList[secondElf] - '0')).toString()

        sumToAdd.forEach { magicList.append(it) }

        val newPositions = Positions(
            firstElf = getNewPosition(firstElf, magicList),
            secondElf = getNewPosition(secondElf, magicList)
        )

        return cycle2(newPositions, magicList)
    }

    return cycle2(startPositions, startingRecipes).count()
}
