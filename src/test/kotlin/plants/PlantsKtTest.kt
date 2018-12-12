package plants

import getData
import org.amshove.kluent.shouldEqual
import org.junit.Test

class PlantsKtTest {

    private val initialData = getData("src/test/resources/plants_initial_data.txt")
    private val realInitialData = getData("src/main/resources/plants_initial_data.txt")

    private val data = getData("src/test/resources/plants_data.txt")
    private val realData = getData("src/main/resources/plants_data.txt")

    @Test
    fun parseInitial() {
        parseInitial(initialData).let { println(it.toList()) }
    }

    @Test
    fun `Should parse plants behavior into patterns which cause plant growth`(){
        println(parseBehavior(data))
    }

    @Test
    fun `Should turn initial data into patterns doniczka by doniczka`(){
        getWorkArray(30)
            .let { putInitialIntoWorkArray(plants.parseInitial(initialData), it) }
            .let {
                getNewGeneration(
                    initialArray = it,
                    workArray = getWorkArray(30),
                    positivePatterns = parseBehavior(data),
                    index = 2) }
            .map { plantPresent -> if(plantPresent) '#' else '.' }
            .joinToString(separator = "")
            .let { println(it) }

    }

    @Test
    fun `Should turn initial data into output data in 20 iterations`(){
        getNGeneration(20, initialData, data, 100)
//            .map { plantPresent -> if(plantPresent) '#' else '.' }
//            .joinToString(separator = "")
//            .let { println(it) }
    }

    @Test
    fun `Should return correct magic number`(){
        getNGeneration(19, initialData, data, 100)
            .let { getMagicNumber(it, plants.parseInitial(initialData)) shouldEqual 325 }
    }

    @Test
    fun `Get number for puzzle`(){
        getNGeneration(131, realInitialData, realData, 600)
            .let { getMagicNumber(it, plants.parseInitial(realInitialData)) }
            .let { print(it) }
    }


}