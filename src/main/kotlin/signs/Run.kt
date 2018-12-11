package signs

fun waitForSign(signs: List<String>) {
    signs
        .map { parseLine(it) }
        .let { Map(it).watchForSign(100000, 0) }
}

fun parseLine(line: String): Point {
    return line.split('<', '>')
        .let { it[1] to it[3] }
        .let { (positions, velocities) -> positions.split(',') to velocities.split(',') }
        .let { (positions, velocities) -> positions.map { it.trim().toInt() } to velocities.map { it.trim().toInt() } }
        .let { (positions, velocities) ->
            Point(
                startX = positions.first(),
                startY = positions[1],
                velocityX = velocities.first(),
                velocityY = velocities[1]
            )
        }
}
