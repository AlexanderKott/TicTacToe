package ru.netology.funnytictactoe

data class Game(var firstPlayerNext : Boolean,
                var xScores : Int,
                var oScores: Int,
                val fieldVariants : Array<Array<String>>,
                var gameField : Array<Array<String>>)


fun initGame() : Game {
    val field = Array(3) {arrayOf("_","_","_")}
    return Game(true,0,0, initVariants(),  field)
}

data class GemeResult(var weHaveWinner: Boolean, var winner: Int, var WinnersRow : Array<Int>)