package plants

fun parseInitial(data: List<String>): BooleanArray = data.first().map { it == '#' }.toBooleanArray()

fun parseBehavior(data: List<String>) = data
    .map { line -> line.split("=>") }
    .map { it.map { it.trim() } }
    .filter { (_, result) -> result == "#" }
    .map { (pattern, _) -> pattern }
    .toHashSet()

fun getWorkArray(howBig: Int) = BooleanArray(howBig)

fun putInitialIntoWorkArray(initial: BooleanArray, workArray: BooleanArray): BooleanArray {
    val startPuttingDataOnIndex = getStartingIndexInBigArray(workArray, initial)

    tailrec fun putData(workArrayIndex: Int, initialArrayIndex: Int) {
        if (initialArrayIndex == initial.size) return
        else {
            workArray[workArrayIndex] = initial[initialArrayIndex]
            putData(workArrayIndex + 1, initialArrayIndex + 1)
        }
    }
    putData(workArrayIndex = startPuttingDataOnIndex, initialArrayIndex = 0)
    return workArray
}

private fun getStartingIndexInBigArray(workArray: BooleanArray, initial: BooleanArray): Int {
    val middleIndex = workArray.size / 2
    val howFarFormMiddleSHouldStart = initial.size / 2
    return middleIndex - howFarFormMiddleSHouldStart
}

tailrec fun getNewGeneration(
    initialArray: BooleanArray,
    workArray: BooleanArray,
    positivePatterns: Set<String>,
    index: Int
): BooleanArray =
    if (index == initialArray.lastIndex - 2) workArray
    else {
        val surround = getSurround(index)
        val willGrow = initialArray
            .copyOfRange(surround.first, surround.second + 1)
            .joinToString(separator = "") { plantPresent -> if (plantPresent) "#" else "." }
            .let { positivePatterns.contains(it) }

        workArray[index] = willGrow
        getNewGeneration(
            initialArray = initialArray,
            workArray = workArray,
            positivePatterns = positivePatterns,
            index = index + 1
        )
    }

fun getNGeneration(n: Long, initialData: List<String>, rawPatterns: List<String>, expectedSpace: Int = 30): BooleanArray {
    val initialArray = getWorkArray(expectedSpace)
        .let { putInitialIntoWorkArray(plants.parseInitial(initialData), it) }

    val initialWorkArray = getWorkArray(expectedSpace)

    val patterns = parseBehavior(rawPatterns)

    tailrec fun processGenerations(previousArray: BooleanArray, workArray: BooleanArray, currentIndex: Int): BooleanArray =
        if (currentIndex > n) previousArray
        else {
            val readyGeneration = getNewGeneration(
                initialArray = previousArray,
                workArray = workArray,
                positivePatterns = patterns,
                index = 2
            ).also {
                it.map { plantPresent -> if (plantPresent) '#' else '.' }
                    .joinToString(separator = "")
                    .let { println("${currentIndex + 1}: $it") }
            }

            processGenerations(
                previousArray = readyGeneration,
                workArray = previousArray,
                currentIndex = currentIndex + 1
            )
        }

    return processGenerations(initialArray, initialWorkArray, 0)
}

fun getMagicNumber(doniczkiArray: BooleanArray, initial: BooleanArray): Int {
    val originalStartingIndex = getStartingIndexInBigArray(doniczkiArray, initial)

    tailrec fun traverse(currentIndex: Int, sum: Int): Int =
        if (currentIndex == doniczkiArray.size) sum
        else {
            val plantValue =
                doniczkiArray[currentIndex].let { plantPresent -> if (plantPresent) currentIndex - originalStartingIndex else 0 }
            traverse(currentIndex + 1, sum + plantValue)
        }

    return traverse(currentIndex = 0, sum = 0)
}

private fun getSurround(index: Int) = index - 2 to index + 2
