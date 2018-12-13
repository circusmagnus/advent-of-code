package carts

enum class Turn{LEFT, STRAIGHT, RIGHT}

enum class Direction(val mapMark: Char) {
    UP('^'){
        override fun makeTurn(turn: Char) = if (turn == '/') RIGHT else LEFT
        override fun makeTurn(turn: Turn): Direction = when(turn){
            Turn.STRAIGHT -> this
            Turn.LEFT     -> LEFT
            Turn.RIGHT    -> RIGHT
        }
    },
    DOWN('v'){
        override fun makeTurn(turn: Char) = if (turn == '/') LEFT else RIGHT
        override fun makeTurn(turn: Turn): Direction = when(turn){
            Turn.STRAIGHT -> this
            Turn.LEFT     -> RIGHT
            Turn.RIGHT    -> LEFT
        }
    },
    LEFT('<'){
        override fun makeTurn(turn: Char) = if (turn == '/') DOWN else UP
        override fun makeTurn(turn: Turn): Direction = when(turn){
            Turn.STRAIGHT -> this
            Turn.LEFT     -> DOWN
            Turn.RIGHT    -> UP
        }
    },
    RIGHT('>'){
        override fun makeTurn(turn: Char) = if (turn == '/') UP else DOWN
        override fun makeTurn(turn: Turn): Direction = when(turn){
            Turn.STRAIGHT -> this
            Turn.LEFT     -> UP
            Turn.RIGHT    -> DOWN
        }
    };

    abstract fun makeTurn(turn: Char): Direction
    abstract fun makeTurn(turn: Turn): Direction
}