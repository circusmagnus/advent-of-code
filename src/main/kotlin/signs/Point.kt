package signs

class Point(startX: Int, startY: Int, private val velocityX: Int, private val velocityY: Int) {

    var positionX: Int = startX
        private set

    var positionY: Int = startY
        private set

    fun updatePosition(){
        positionX += velocityX
        positionY += velocityY
    }

    fun hasNeighbours(otherPoints: Collection<Point>): Boolean = otherPoints
        .filter { (it.positionX - this.positionX) in -1..1 }
        .filter { (it.positionY - this.positionY) in -1..1 }
        .filter { other -> other.positionX != this.positionX || other.positionY != this.positionY }
        .isNotEmpty()
}