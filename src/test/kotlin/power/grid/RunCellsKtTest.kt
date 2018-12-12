package power.grid

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Assert.*

import org.junit.Test

class RunCellsKtTest {

    @Test
    fun `createPowerCells does not crash`() {
        val cells = createPowerCells()
        cells.size shouldEqual 90000
    }

    @Test
    fun `get Best Grid`() {
        getBestGrid(3)?.let { (x, y) -> print("x: $x, y: $y") }

    }

    @Test
    fun `get Best Grid size`() {
        getBestGridSize()?.let { grid -> print("x: ${grid.x}, y: ${grid.y}, grid size: ${grid.size}, grid power: ${grid.powerLevel}") }

    }

}