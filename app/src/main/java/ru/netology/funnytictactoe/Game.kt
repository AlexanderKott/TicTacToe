package ru.netology.funnytictactoe

import android.graphics.Point

class Game(var firstPlayerNext : Boolean,
                var xScores : Int,
                var oScores: Int,
                val fieldVariants : Array<Array<String>>,
                var gameField : Array<Array<String>>,
                var cheating: Boolean)


fun initGame() : Game {
    val field = Array(3) {arrayOf("_","_","_")}
    return Game(true,0,0, initVariants(),  field, false)
}

class GameResult(var weHaveWinner: Boolean, var winner: Int, var WinnersRow : Array<Int>)

class OTag (val number: Int ,val point : Point)