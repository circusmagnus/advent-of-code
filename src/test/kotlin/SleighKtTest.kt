import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.junit.Assert.*
import org.junit.Test

class SleighKtTest {

    private val data = getData("src/test/resources/sleigh_data.txt")

    private val realData = getData("src/main/resources/sleigh_data.txt")

    @Test
    fun `Should build id-requirements map`() {
        getIdRequiresMap(data) shouldContainAll mapOf(
            'C' to emptyList<Char>(),
            'A' to listOf('C'),
            'B' to listOf('A'),
            'E' to listOf('B', 'D', 'F'),
            'D' to listOf('A'),
            'F' to listOf('C')
        )
    }

    @Test
    fun `asembleSleigh2 should work`() {
        assembleSleigh2(data) shouldEqual "CABDFE"

        assembleSleigh2(realData).let { print(it) }
    }

    @Test
    fun `Should get correct additonal time for task`() {
        getAdditionalTimeForTask('A') shouldBe 1
        getAdditionalTimeForTask('C') shouldBe 3
        getAdditionalTimeForTask('Z') shouldBe 26
    }

    @Test
    fun `asembleSleigh3, with 2 workers and 0 base time should work`() {
        assembleSleigh3(
            data,
            baseWorkTime = 0,
            workersCount = 2
        ) shouldEqual 15

    }

    @Test
    fun `asembleSleigh3, with 5 workers and 60 base time should work`() {
        assembleSleigh3(
            realData,
            baseWorkTime = 60,
            workersCount = 5
        ).let { print(it) }
    }
}