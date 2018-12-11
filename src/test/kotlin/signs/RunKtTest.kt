package signs

import getData
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Assert.*

import org.junit.Test

class RunKtTest {

    private val data = getData("src/test/resources/signs_data.txt")

    private val realData = getData("src/main/resources/signs_data.txt")

    @Test
    fun waitForSign() {
        waitForSign(data)
    }

    @Test
    fun waitForSignForReal() {
        waitForSign(realData)
    }

    @Test
    fun parseLine() {
        signs.parseLine(data.first()).positionX shouldBe 9
        signs.parseLine(data.first()).positionY shouldBe 1

        signs.parseLine(data.first())
            .apply { updatePosition() }
            .positionX shouldBe 9

        signs.parseLine(data.first())
            .apply { updatePosition() }
            .positionY shouldBe 3

    }

    @Test
    fun parseLine2() {
        signs.parseLine(data[2]).positionX shouldBe 3
        signs.parseLine(data[2]).positionY shouldBe -2

        signs.parseLine(data[2])
            .apply { updatePosition() }
            .positionX shouldBe 2

        signs.parseLine(data[2])
            .apply { updatePosition() }
            .positionY shouldBe -1

    }
}