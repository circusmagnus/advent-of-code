import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Assert.*

import org.junit.Test

class BoxIdsKtTest {

    @Test
    fun `Should find checksum in test data`() {
        val data = getData("src/test/resources/box_ids_test.txt")

        findChecksum(data) shouldBe 12
    }

    @Test
    fun `Find checksum in performance test`() {
        getProbableBoxIds("src/main/resources/box_ids.txt")
    }

    @Test
    fun `Should find recurring chars`() {
        val data = getData("src/test/resources/box_ids_similarity_test.txt")
        findConstantChars(data) shouldEqual "fgij"

    }

    @Test
    fun `Find recurring chars performance test`() {
        getBoxBaseId("src/main/resources/box_ids.txt")
    }
}