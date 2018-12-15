package chocolate

import org.amshove.kluent.shouldEqual
import org.junit.Assert.*

import org.junit.Test

class ChocolateKtTest {

//    @Test
//    fun getNewPosition() {
//        val recipes = "3710101245"
//        val positions = Positions(4, 8)
//
//        getNewPosition(positionIndex = positions.firstElf, recipes = recipes) shouldEqual 6
//        getNewPosition(positionIndex = positions.secondElf, recipes = recipes) shouldEqual 3
//
//    }

    @Test
    fun `Does it work`(){
        chocolate.run2("59414") shouldEqual 2018
    }

    @Test
    fun `get result`(){
        chocolate.run2("635041").let { println(it) }
    }
}