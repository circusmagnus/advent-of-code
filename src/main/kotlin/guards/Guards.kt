package guards

import java.time.LocalDateTime

fun parseToDatesAndNotes(rawData: List<String>) = rawData
    .asSequence()
    .map { it.split('[', ']') }
    .map { it.drop(1) }
    .map { it.first() to it[1].trimStart() }
    .sortedBy { it.first }

fun parseDates(rawDate: String): LocalDateTime{
    return  LocalDateTime.parse(rawDate)
}

sealed class Event{
    abstract val guardId: Int
    data class BeginShift(override val guardId: Int) : Event()
    data class FallAsleep(override val guardId: Int) : Event()
    data class WakeUp(override val guardId: Int) : Event()
}