package carts

class Cart(x: Int, y: Int, direction: Char, private val map: Array<CharArray>){
    var positionX = x
    private set

    var positionY = y
    private set

    var isCrashed = false
    private set

    private var cartDirection = Direction.values().first { it.mapMark == direction }

    private var lastTurn = Turn.RIGHT

    var trackUnderCart = when(cartDirection){
        Direction.UP, Direction.DOWN    -> '|'
        Direction.LEFT, Direction.RIGHT -> '-'
    }
    private set

    fun move(){
        val (newX, newY) = getNewPosition()
        releasePreviousTrack()
        isCrashed = map[newY][newX].let { Direction.values().map { it.mapMark }.contains(it) }
        positionY = newY; positionX = newX
        if (isCrashed) return
        saveCurrentTrack()
        cartDirection = getNewDirection(newX, newY)
        updateMap()
    }

    private fun updateMap(){
        map[positionY][positionX] = cartDirection.mapMark
    }

    private fun releasePreviousTrack(){
        map[positionY][positionX] = trackUnderCart
    }

    private fun saveCurrentTrack(){
        trackUnderCart = map[positionY][positionX]
    }

    private fun getNewDirection(x: Int, y: Int) = map[y][x].let { newTrack ->
        when(newTrack){
            '-', '|' -> cartDirection
            '+' -> getNextTurn().let { cartDirection.makeTurn(it) }
            else -> cartDirection.makeTurn(newTrack)
        }
    }

    private fun getNextTurn() = when(lastTurn){
        Turn.RIGHT    -> {
            lastTurn = Turn.LEFT
            Turn.LEFT
        }
        Turn.LEFT     -> {
            lastTurn = Turn.STRAIGHT
            Turn.STRAIGHT
        }
        Turn.STRAIGHT -> {
            lastTurn = Turn.RIGHT
            Turn.RIGHT
        }
    }

    private fun getNewPosition() = when(cartDirection){
        Direction.UP    -> positionX to positionY - 1
        Direction.DOWN  -> positionX to positionY + 1
        Direction.LEFT  -> positionX -1 to positionY
        Direction.RIGHT -> positionX + 1 to positionY
    }
}