package power.grid

import org.amshove.kluent.shouldBe
import org.junit.Assert.*

import org.junit.Test

class CellTest {

    @Test
    fun getPowerLevel() {
        Cell(3, 5, 8).powerLevel shouldBe 4
        Cell(217, 196, 39).powerLevel shouldBe 0
    }

    @Test
    fun getGridPowerLevel() {
        Cell(3, 5, 8).powerLevel shouldBe 4
    }
}