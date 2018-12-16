package goblins

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
        if (otherObject is Goblin) Interaction.FIGHT
        else Interaction.OBSTACLE

    override fun getSymbol(): Char = 'E'
}

object Goblin : MapObject {
    override fun getInteraction(otherObject: MapObject): Interaction =
        if (otherObject is Elf) Interaction.FIGHT
        else Interaction.OBSTACLE

    override fun getSymbol(): Char = 'G'
}

object NodeMark : MapObject {
    override fun getInteraction(otherObject: MapObject): Interaction = Interaction.OBSTACLE
    override fun getSymbol(): Char = 'N'
}

interface Warrior {
    fun sufferWound(howBad: Int)
    fun strike(other: Warrior)
}

class Warrior1(val power: Int, startHp: Int) : Warrior {
    var hp = startHp
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
    private val position: Position,
    private val mapObject: MapObject,
    private val warrior: Warrior
) : MapObject by mapObject, Warrior by warrior {

    fun move(map: Array<Array<MapObject?>>): Unit = TODO()

    fun findNextStep(map: Array<Array<MapObject?>>): Position? {

        tailrec fun getLastNode(nodesInCycle: List<Node>): Node? {
            if (nodesInCycle.isEmpty()) return null

            val childrenNodes = nodesInCycle
                .map { node ->
                    node.getChildren(map)
                        .takeUnless { it == null }
                        ?: return node
                }.flatten()
                .sortedWith( Comparator { o1, o2 ->
                    (o1.position.y - o2.position.y).takeUnless { it == 0 } ?: o1.position.x - o2.position.x
                })

            return getLastNode(childrenNodes)
        }

        tailrec fun getOriginNode(node: Node): Node =
            if (node.parent == null) node
            else getOriginNode(node.parent)

        val startingNodes = getAdjacentLocations(position.x, position.y)
            .map { Node(it, null, this) }

        val closestEnemy = getLastNode(startingNodes)
        val nextStep = closestEnemy?.let { getOriginNode(it) }

        return nextStep?.position
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

fun getMap(rawData: List<String>): Array<Array<MapObject?>> = Array(32) { yIndex ->
    Array(32) { xIndex ->
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