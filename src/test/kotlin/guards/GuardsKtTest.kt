package guards

import getData
import org.junit.Assert.*

import org.junit.Test

class GuardsKtTest {

    private val data = getData("src/test/resources/guards_data.txt")

    private val realData = getData("src/main/resources/guards_data.txt")

    @Test
    fun parseToDatesAndNotes() {
        guards.parseToDatesAndNotes(data).let { println(it.toList()) }
    }

    @Test
    fun parseDates() {
    }
}