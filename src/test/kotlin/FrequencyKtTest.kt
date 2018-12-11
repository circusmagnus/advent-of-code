import org.amshove.kluent.shouldBe
import org.junit.Assert.*

import org.junit.Test

class FrequencyKtTest {

    @Test
    fun `Should sum everything`() {
        val data = listOf(1, 2, -4)

        computeFrequency(data) shouldBe -1
    }

    @Test
    fun `Should sum everything in file`() {
        getFrequency("src/test/resources/freq_data_test.txt")
            .let { computeFrequency(it) } shouldBe -1
    }

    @Test
    fun `Should get first double frequency`(){
        val data = listOf(+3, +3, +4, -2, -4)

        computeStableFreq(data) shouldBe 10
    }

    @Test
    fun `Should get first double frequency 2`(){
        val data = listOf(+7, +7, -2, -7, -4)

        computeStableFreq(data) shouldBe 14
    }

    @Test
    fun `Performance test of stable freq`(){

        chronalCalibration("src/main/resources/freq_data.txt") {
            computeStableFreq(it)
        }
    }
}