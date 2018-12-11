import java.util.Deque
import java.util.LinkedList

fun findChecksum(data: List<String>): Int {
    val lines = LinkedList<String>().apply { addAll(data) }

    tailrec fun scanLine(lines: Deque<String>, doubles: Int, triples: Int): Int {
        if (lines.isEmpty()) return doubles * triples
        val charCountsMap = lines.pop().groupBy { it }
        val lineDoubles = charCountsMap.values.filter { it.size == 2 }.take(1).count()
        val lineTriples = charCountsMap.values.filter { it.size == 3 }.take(1).count()
        return scanLine(lines, doubles = doubles + lineDoubles, triples = triples + lineTriples)
    }

    return scanLine(lines, 0, 0)
}

fun getProbableBoxIds(path: String) = findChecksum(getData(path)).also { println(it) }

fun findConstantChars(data: List<String>): String {

    tailrec fun getStringsWithoutOneLetter(
        text: String,
        indexToDrop: Int,
        stringsWithoutOneLetter: Map<Int, String>
    ): Map<Int, String> {
        if (indexToDrop > text.lastIndex) return stringsWithoutOneLetter
        val newEntry = indexToDrop to text.filterIndexed { index, _ -> index != indexToDrop }
        return getStringsWithoutOneLetter(
            text,
            indexToDrop + 1,
            stringsWithoutOneLetter + newEntry
        )
    }

    return data
        .map { getStringsWithoutOneLetter(it, 0, emptyMap()) }
        .flatMap { it.entries }
        .groupBy { it }
        .filterValues { it.size > 1 }
        .also { println(it) }
        .flatMap { it.value }
        .first().value

//    val listOfMaps = data.map { getStringsWithoutOneLetter(it, 0, emptyMap()) }
//
//    fun getMatchingString(index: Int) : String{
//        val doMatch = listOfMaps.map { it[index] }
}

fun getBoxBaseId(path: String) = findConstantChars(getData(path)).also { println(it) }


