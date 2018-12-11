package signs

import org.amshove.kluent.shouldBe
import org.junit.Assert.*

import org.junit.Test

class PointTest {

    @Test
    fun `Point correctly reports when has neighbours`() {
        val testedPoint = Point(startX = 5, startY = 5, velocityX = 0, velocityY = 0)

        val otherPointsWithOneNeighbours = listOf(
            Point(startX = 5, startY = 4, velocityY = 0, velocityX = 0),
            Point(startY = 1, startX = 1, velocityX = 0, velocityY = 0)
        )

        testedPoint.hasNeighbours(otherPointsWithOneNeighbours) shouldBe true
    }

    @Test
    fun `Point correctly reports when has no neighbours`() {
        val testedPoint = Point(startX = 5, startY = 5, velocityX = 0, velocityY = 0)

        val otherPointsWithOneNeighbours = listOf(
            Point(startX = 5, startY = 3, velocityY = 0, velocityX = 0),
            Point(startY = 1, startX = 1, velocityX = 0, velocityY = 0)
        )

        testedPoint.hasNeighbours(otherPointsWithOneNeighbours) shouldBe false
    }
}