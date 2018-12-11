fun getOverlapPoints(claims: List<String>): Pair<Int, Int> = claims
    .map { convertStringToClaim(it) }
    .let { putClaimsOnFabric(it) }
    .let { (_, conflicts, uncontestedId) -> conflicts to uncontestedId }

data class Claim(val id: Int, val fromLeft: Int, val fromTop: Int, val width: Int, val height: Int)

fun convertStringToClaim(string: String): Claim = string
    .split("#", "@", ",", ":", "x")
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .map { it.toInt() }
    .let { (id, fromLeft, fromTop, width, height) -> Claim(id, fromLeft, fromTop, width, height) }

fun createFabric(claims: List<Claim>): Array<IntArray> {

    val fabricWidth = claims.maxBy { it.fromLeft + it.width }?.let { it.fromLeft + it.width } ?: 0
    val fabricHeight = claims.maxBy { it.fromTop + it.height }?.let { it.fromTop + it.height } ?: 0

    return Array(fabricWidth) { IntArray(fabricHeight) }
}

fun putClaimsOnFabric(claims: List<Claim>): Triple<Array<IntArray>, Int, Int> {
    val fabric = createFabric(claims)

    var claimConflicts = 0

    val unContestedClaims = claims.groupBy { it.id }.toMutableMap()

    claims
        .forEach { (id, fromLeft, fromTop, width, height) ->
            val lateralPositionRange = fromLeft..(fromLeft + width - 1)
            val heightPositionRange = fromTop..(fromTop + height - 1)
            var isUncontested = true

            for (outerPosition in lateralPositionRange){
                for (innerPosition in heightPositionRange){

                    fabric[outerPosition][innerPosition].let {oldId ->
                        if (oldId == 0 ) fabric[outerPosition][innerPosition] = id
                        else if (oldId == -1) {
                            isUncontested = false
                        }
                        else {
                            unContestedClaims.remove(oldId)
                            fabric[outerPosition][innerPosition] = -1
                            claimConflicts += 1
                            isUncontested = false
                        }
                    }
                }
            }

            if (!isUncontested) unContestedClaims.remove(id)
        }
    return Triple(fabric, claimConflicts, unContestedClaims.keys.first())
}