package goblins

import java.util.ArrayDeque
import java.util.Deque

enum class Interaction { FIGHT, OBSTACLE }

interface MapObject {
    fun getInteraction(otherObject: MapObject): Interaction
    fun getSymbol(): Char
}

object Wall : MapObject {
    override fun getInteraction(otherObject: MapObject): Interaction = Interaction.OBSTACLE
    override fun getSymbol(): Char = '#'
}

object Elf : MapObject {
    override fun getInteraction(otherObject: MapObject): Interaction =
        if (otherObject.getSymbol() == 'G') Interaction.FIGHT
        else Interaction.OBSTACLE

    override fun getSymbol(): Char = 'E'
}

object Goblin : MapObject {
    override fun getInteraction(otherObject: MapObject): Interaction =
        if (otherObject.getSymbol() == 'E') Interaction.FIGHT
        else Interaction.OBSTACLE

    override fun getSymbol(): Char = 'G'
}

object NodeMark : MapObject {
    override fun getInteraction(otherObject: MapObject): Interaction = Interaction.OBSTACLE
    override fun getSymbol(): Char = 'N'
}

interface Warrior {
    val hp: Int
    fun sufferWound(howBad: Int)
    fun strike(other: Warrior)
}

class Warrior1(val power: Int, startHp: Int) : Warrior {
    override var hp = startHp
        private set

    override fun sufferWound(howBad: Int) {
        hp -= howBad
    }

    override fun strike(other: Warrior) {
        other.sufferWound(power)
    }
}

data class Position(val x: Int, val y: Int)

class Player(
    position: Position,
    private val mapObject: MapObject,
    private val warrior: Warrior
) : MapObject by mapObject, Warrior by warrior {

    var position = position
    private set

    var hasWonTheGame = false
    private set

    fun makeMove(map: Array<Array<MapObject?>>) {
        findNextStep(map)
            ?.let { (newX, newY) ->
                position.let { (x, y) -> map[y][x] = null }
                position = Position(newX, newY)
                map[newY][newX] = this
            } ?: kotlin.run { hasWonTheGame = true }

        findEnemy(map)?.let { enemy ->
            strike(enemy)
            if (enemy.hp <= 0) enemy.position.let { (x, y) -> map[y][x] = null }
        }
    }

    fun findEnemy(map: Array<Array<MapObject?>>) = position
        .let { (x, y) -> getAdjacentLocations(x, y) }
        .filter { (x, y) -> map[y][x]?.getInteraction(this) == Interaction.FIGHT }
        .map { (x, y) -> (map[y][x] as Player) }
        .sortedWith(Comparator { o1, o2 ->
            (o1.hp - o2.hp).takeUnless { it == 0 }
                ?: (o1.position.y - o2.position.y).takeUnless { it == 0 }
                ?: o1.position.x-o2.position.x
        })
        .firstOrNull()

    fun findNextStep(map: Array<Array<MapObject?>>): Position? {

        val areThereEnemies = findPlayers(map)
            .filter { it.getInteraction(this) == Interaction.FIGHT }.isNotEmpty()

        if(areThereEnemies.not()) return null

        tailrec fun getLastNode(nodesInCycle: List<Node>): Node? {
            if (nodesInCycle.isEmpty()) return null

            val childrenNodes = nodesInCycle
                .map { node ->
                    node.getChildren(map)
                        .takeUnless { it == null }
                        ?: return node
                }.flatten()
                .sortedWith(Comparator { o1, o2 ->
                    (o1.position.y - o2.position.y).takeUnless { it == 0 } ?: o1.position.x-o2.position.x
                })

            return getLastNode(childrenNodes)
        }

        tailrec fun getOriginNode(node: Node): Node =
            if (node.parent?.parent == null) node
            else getOriginNode(node.parent)

        val startingNode = Node(position, null, this)

        val closestEnemy = getLastNode(listOf(startingNode))
        val nextStep = closestEnemy?.let { getOriginNode(it) } ?: startingNode

        return nextStep.position.also { clearNodeMarks(map) }
    }

    fun clearNodeMarks(map: Array<Array<MapObject?>>){
        for (yIndex in 0 until map.size){
            for (xIndex in 0 until map[yIndex].size){
                val thing = map[yIndex][xIndex]
                if(thing?.getSymbol() == 'N'){
                    map[yIndex][xIndex] = null
                }
            }
        }
    }

    override fun toString(): String {
        return "${getSymbol()}, $position, $hp"
    }
}

class Node(val position: Position, val parent: Node?, val origin: Player) {

    fun getChildren(map: Array<Array<MapObject?>>): List<Node>? {
        val children = mutableListOf<Node>()
        val locationsToCheck = position.let { (x, y) ->
            getAdjacentLocations(x, y)
        }
        for ((x, y) in locationsToCheck) {
            if (Position(x, y) == parent?.position) continue
            val interaction = map[y][x]?.getInteraction(origin)
            if (interaction == null) {
                children.add(Node(Position(x, y), this, origin))
                map[y][x] = NodeMark
            } else if (interaction == Interaction.FIGHT) return null
        }
        return children
    }
}

private fun getAdjacentLocations(x: Int, y: Int): List<Position> = listOf(
    Position(x, y - 1),
    Position(x - 1, y),
    Position(x + 1, y),
    Position(x, y + 1)
)

fun getMap(rawData: List<String>): Array<Array<MapObject?>> = Array(rawData.size) { yIndex ->
    Array(rawData[yIndex].length) { xIndex ->
        rawData[yIndex][xIndex].let { char ->
            when (char) {
                '#'  -> Wall
                'E'  -> Player(Position(xIndex, yIndex), Elf, Warrior1(3, 200))
                'G'  -> Player(Position(xIndex, yIndex), Goblin, Warrior1(3, 200))
                else -> null
            }
        }
    }
}

fun printMap(map: Array<Array<MapObject?>>) {

    for (y in 0 until map.size) {
        for (x in 0 until map[y].size) {
            if (x == map[y].lastIndex) println(map[y][x]?.getSymbol() ?: '.')
            else print(map[y][x]?.getSymbol() ?: '.')
        }
    }
}

fun findPlayers(map: Array<Array<MapObject?>>): List<Player> {

    val players = mutableListOf<Player>()

    for (row in map) {
        row.filterNotNull()
            .filter { it.getSymbol() == 'G' || it.getSymbol() == 'E' }
            .map { it as Player }
            .sortedWith(Comparator { o1, o2 ->
                (o1.position.y - o2.position.y).takeUnless { it == 0 } ?: o1.position.x-o2.position.x
            })
            .forEach { players.add(it) }
    }

    return players
}

fun runGame(rawData: List<String>): Int {
    val map = getMap(rawData)

    tailrec fun runRound(players: Deque<Player>): Boolean{
        if(players.isEmpty()) return false
        val player = players.pop()
        player.makeMove(map)

        return if(player.hasWonTheGame) true
        else runRound(players)
    }

    tailrec fun runGame(turn: Int): Int {
        val playersThisRound = findPlayers(map)
        val didFinishGame = runRound(ArrayDeque(playersThisRound))

        println("round: $turn")
        printMap(map)

        return if(didFinishGame) turn
        else runGame(turn + 1)
    }

    return runGame(0) * (findPlayers(map).sumBy { it.hp })
}

