import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Assert.*
import org.junit.Test

class FabricKtTest{

    @Test
    fun `Get correct number of points and uncontested id from real data`(){
        val data = getData("src/main/resources/fabric_data.txt")

        println(getOverlapPoints(data))
    }

    @Test
    fun `Should get correct number of points`(){
        val data = getData("src/test/resources/fabric_data.txt")

        getOverlapPoints(data).first shouldBe 4
    }

    @Test
    fun `Should convert easy string to claim`(){
        val string = "#123 @ 3,2: 5x4"

        convertStringToClaim(string) shouldEqual Claim(
            id = 123,
            fromLeft = 3,
            fromTop = 2,
            width = 5,
            height = 4
        )
    }

    @Test
    fun `Should convert hard string to claim`(){
        val string = "#961 @ 414,803: 11x22"

        convertStringToClaim(string) shouldEqual Claim(
            id = 961,
            fromLeft = 414,
            fromTop = 803,
            width = 11,
            height = 22
        )
    }

    @Test
    fun `Should create fabric`(){
        val claims = getData("src/test/resources/fabric_data.txt")
            .map { convertStringToClaim(it) }

        createFabric(claims).size shouldEqual 7
        createFabric(claims)[0].size shouldEqual 7
    }

    @Test
    fun `Should put claims ids in fabric`(){
        val claims = getData("src/test/resources/fabric_data.txt")
            .map { convertStringToClaim(it) }

        val (fabric, _, _) = putClaimsOnFabric(claims)

        fabric[1][3] shouldBe 1
        fabric[5][5] shouldBe 3

    }

    @Test
    fun `Should put -1 in places claimed by more than one elf`(){
        val claims = getData("src/test/resources/fabric_data.txt")
            .map { convertStringToClaim(it) }

        val (fabric, _, _) = putClaimsOnFabric(claims)

        fabric[3][3] shouldBe -1
        fabric[4][3] shouldBe -1
        fabric[3][4] shouldBe -1
        fabric[4][4] shouldBe -1

    }

    @Test
    fun `Should find 4 conflicts`(){
        val claims = getData("src/test/resources/fabric_data.txt")
            .map { convertStringToClaim(it) }

        val (_, conflicts, _) = putClaimsOnFabric(claims)

        conflicts shouldBe 4

    }

    @Test
    fun `Should find the only uncontested claim`(){
        val claims = getData("src/test/resources/fabric_data.txt")
            .map { convertStringToClaim(it) }

        val (_, _, uncontestedId) = putClaimsOnFabric(claims)

        uncontestedId shouldBe 3

    }
}