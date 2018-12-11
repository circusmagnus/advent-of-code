import java.io.File
import java.util.LinkedList

fun computeFrequency(frequencies: List<Int>): Int = frequencies.sum()

fun getFrequency(filePath: String): List<Int> = File(filePath).useLines { sequence ->
    sequence.map { it.toInt() }.toList()
}

inline fun chronalCalibration(filePath: String, how: (List<Int>) -> Int) {
    println(how(getFrequency(filePath)))
}

fun computeStableFreq(frequencies: List<Int>): Int {

    val controlSet = HashSet<Int>()
    val workList = LinkedList<Int>()

    tailrec fun getRepeatedFreq(lastSum: Int): Int {
        if (workList.isEmpty()) {
            workList.addAll(frequencies)
        }

        val newSum = lastSum + workList.pop()
        val didAdd = controlSet.add(newSum)

        return if (didAdd.not()) newSum
        else getRepeatedFreq(newSum)
    }

    return getRepeatedFreq(0)
}

fun main() {
    chronalCalibration("src/main/resources/freq_data.txt") {
        computeFrequency(it)
    }

    chronalCalibration("src/main/resources/freq_data.txt") {
        computeStableFreq(it)
    }
}