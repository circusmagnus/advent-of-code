package carts

import getData
import org.amshove.kluent.shouldEqual
import org.junit.Assert.*

import org.junit.Test

class CartsKtTest {

    private val data = getData("src/test/resources/carts.txt")

    private val realData = getData("src/main/resources/carts.txt")

    @Test
    fun parseIntoMap() {
    }

    @Test
    fun findCarts() {
    }

    @Test
    fun run() {
        carts.run(data) shouldEqual Pair(7, 3)
    }

    @Test
    fun runForReal() {
        carts.run(realData).let { println(it) }
    }
}