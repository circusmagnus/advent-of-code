package goblins

import getData
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Assert.*

import org.junit.Test

class PlayerTest {

    val easyTargeting = getData("src/test/resources/goblins/targeting_easy.txt")
    val manyTargets = getData("src/test/resources/goblins/manyTargets.txt")
    val targetInRange = getData("src/test/resources/goblins/target_in_range.txt")
    val blockedPath = getData("src/test/resources/goblins/blocked_path.txt")
    val manyEnemies = getData("src/test/resources/goblins/many_enemies.txt")
    val noEnemiesOnMap = getData("src/test/resources/goblins/no_enemies.txt")
    val testGame1 = getData("src/test/resources/goblins/test_game.txt")
    val realGame1 = getData("src/test/resources/goblins/real_game1.txt")




    @Test
    fun move() {
    }

    @Test
    fun `Should find next step on easy map`() {
        val playGround = getMap(easyTargeting)
        printMap(playGround)
        val elf = playGround[1][1] as Player
        println(elf)

        elf.findNextStep(playGround) shouldEqual Position(2, 1)
    }

    @Test
    fun `Should find next step on map with many equidistant targets`() {
        val playGround = getMap(manyTargets)
        printMap(playGround)
        val elf = playGround[4][4] as Player
        println(elf)

        elf.findNextStep(playGround) shouldEqual Position(4, 3)
    }

    @Test
    fun `Next step should equal current position if target in range`() {
        val playGround = getMap(targetInRange)
        printMap(playGround)
        val elf = playGround[3][4] as Player
        println(elf)

        elf.findNextStep(playGround) shouldEqual Position(4, 3)
    }

    @Test
    fun `Next step should should equal current position if no enemies can be reached`() {
        val playGround = getMap(blockedPath)
        printMap(playGround)
        val goblin = playGround[1][1] as Player
        println(goblin)

        goblin.findNextStep(playGround) shouldEqual Position(1, 1)
    }

    @Test
    fun `Next step should be null if no enemies present on map`() {
        val playGround = getMap(noEnemiesOnMap)
        printMap(playGround)
        val goblin = playGround[1][1] as Player
        println(goblin)

        goblin.findNextStep(playGround) shouldEqual null
    }

    @Test
    fun `Should find enemy with lowest hp`(){
        val playGround = getMap(manyEnemies)
        printMap(playGround)

        val weakGoblin = (playGround[5][5] as Player).apply { sufferWound(3) }
        val healthyGoblin = (playGround[3][5] as Player)
        val elf = (playGround[4][5] as Player)

        elf.findEnemy(playGround) shouldBe weakGoblin
    }

    @Test
    fun `Should find enemy according to xy priority if hp are equal`(){
        val playGround = getMap(manyEnemies)
        printMap(playGround)

        val secondaryGoblin = (playGround[5][5] as Player)
        val primaryGoblin = (playGround[3][5] as Player)
        val elf = (playGround[4][5] as Player)

        elf.findEnemy(playGround) shouldBe primaryGoblin
    }

    @Test
    fun `Should run game on test input`(){
        runGame(testGame1) shouldEqual 27730
    }

    @Test
    fun `Should run game on real input`(){
        runGame(realGame1).let { print(it) }
    }
}