package ru.netology.funnytictactoe

import kotlin.random.Random

fun computerFirstMove(field2D: Array<Array<String>>) {
    field2D[Random.nextInt(2)][Random.nextInt(2)] = "O"
}

fun makeMoveAndCheckConditions(game: Game): Boolean {
    val field2D: Array<Array<String>> = game.gameField
    val variants: Array<Array<String>> = game.fieldVariants

    if (hasEmptyCells(field2D) && !weHaveWinner(game)) {
        println("Computer's move:")
        computerMove(field2D, variants)
        printField(field2D)
        if (weHaveWinner(game) || !hasEmptyCells(field2D)) {
            return false
        }
        return true
    }
    return false
}

fun computerMove(field2D: Array<Array<String>>, variants: Array<Array<String>>) {
    val coords = getComputerMove(field2D, variants)
    field2D[coords[0]][coords[1]] = "O"
}
typealias calcLine = (line: String, iteration: Int) -> Array<Int>

fun getComputerMove(field2D: Array<Array<String>>, variants: Array<Array<String>>): Array<Int> {

    val finishMyRowF: calcLine = { line, i ->
        if (line.count { it == 'O' } == 2 && line.indexOf("_") != -1) {
            mapOfGameField(i, line.indexOf('_'))
        } else arrayOf(-1, -1)
    }

    val solveDangerF: calcLine = { line, i ->
        if (line.count { it == 'X' } == 2 && line.indexOf("_") != -1) {
            mapOfGameField(i, line.indexOf('_'))
        } else arrayOf(-1, -1)
    }

    val continueMyRowF: calcLine = { line, i ->
        if (line.count { it == 'O' } == 1 && line.count { it == '_' } == 2) {
            mapOfGameField(i, line.indexOf('_'))
        } else arrayOf(-1, -1)
    }

    val getFreeLine: calcLine = { line, i ->
        if (line.count { it == '_' } == 3) {
            mapOfGameField(i, line.indexOf('_'))
        } else arrayOf(-1, -1)
    }

    val logic = arrayOf(finishMyRowF, solveDangerF, continueMyRowF, getFreeLine)

    for (func in logic) {
        val fcoords = calcTurn(func, field2D, variants)
        if (fcoords[0] != -1) return fcoords
    }
    return getJustFreeCoords(field2D)
}

fun calcTurn(func: calcLine, field2D: Array<Array<String>>, variants: Array<Array<String>>): Array<Int> {
    for (i in variants.indices) {
        val coord = func.invoke(getRow(variants, i, field2D), i)
        if (coord[0] != -1) return coord
    }
    return arrayOf(-1, -1)
}


fun getJustFreeCoords(field2D: Array<Array<String>>): Array<Int> {
    for (i in field2D.indices)
        for (j in field2D[0].indices) {
            if (field2D[i][j] == "_")
                return arrayOf(i, j)
        }
    return arrayOf(-1, -1)
}

fun mapOfGameField(i: Int, indexOf: Int): Array<Int> {
    if (i in 0..2) return arrayOf(i, indexOf)
    if (i in 3..5) return arrayOf(indexOf, i - 3)
    if (i == 6) return arrayOf(indexOf, indexOf)
    if (i == 7) {
        if (indexOf == 0) return arrayOf(2, 0)
        if (indexOf == 1) return arrayOf(1, 1)
        if (indexOf == 2) return arrayOf(0, 2)
    }
    return arrayOf(-1, -1)
}


fun weHaveWinner(game: Game): Boolean {
    val field2D: Array<Array<String>> = game.gameField
    val variants: Array<Array<String>> = game.fieldVariants
    return isWinner(field2D, variants, 'O') != -1 ||
            isWinner(field2D, variants, 'X') != -1
}

fun getWinner(game: Game): GemeResult {
    val field2D: Array<Array<String>> = game.gameField
    val variants: Array<Array<String>> = game.fieldVariants
    val resultX = isWinner(field2D, variants, 'X')
    val resultO = isWinner(field2D, variants, 'O')
    if (resultX != -1) {
        return GemeResult(true, 0, mapOfUIField(resultX))
    } else if (resultO != -1) {
        return GemeResult(true, 1, mapOfUIField(resultO))
    } else
        return GemeResult(false, -1, emptyArray())
}

fun mapOfUIField(result: Any): Array<Int> {
 return when(result){
      0 -> arrayOf(0,1,2)
      1 -> arrayOf(3,4,5)
      2 -> arrayOf(6,7,8)
      3 -> arrayOf(0,3,6)
      4 -> arrayOf(1,4,7)
      5 -> arrayOf(2,5,8)
      6 -> arrayOf(0,4,8)
      7 -> arrayOf(2,4,3)
      else -> arrayOf()
  }
}


fun hasEmptyCells(field2D: Array<Array<String>>) =
        countSteps(arrayToString(field2D), '_') > 0

fun calcGameResults(game: Game): String {
    val field2D = game.gameField
    val variants = game.fieldVariants

    val output = when {

        isWinner(field2D, variants, 'O') != -1 -> {
            game.oScores += 1
            "O wins"
        }

        isWinner(field2D, variants, 'X') != -1 -> {
            game.xScores += 1
            "X wins"
        }

        isDraw(arrayToString(field2D)) -> "Draw"
        isGameInProcess(arrayToString(field2D)) -> "Game in the process"
        isImpossibleState(field2D, variants) -> "Cheating! Stop it :)"
        else -> "Error"
    }
    return output
}

fun getRow(
        variants: Array<Array<String>>, i: Int,
        field: Array<Array<String>>
): String {
    val a = getCoordFromStr(variants, i, 0)
    val b = getCoordFromStr(variants, i, 1)
    val c = getCoordFromStr(variants, i, 2)
    return "${field[a[0]][a[1]]}${field[b[0]][b[1]]}${field[c[0]][c[1]]}"
}

fun isGameInProcess(input: String): Boolean =
        countSteps(input, '_') > 0 && isValidCountOfSteps(input)

fun isDraw(input: String): Boolean =
        isValidCountOfSteps(input) && countSteps(input, '_') < 1

fun isImpossibleState(
        field2D: Array<Array<String>>,
        variants: Array<Array<String>>
): Boolean {
    val o = isWinner(field2D, variants, 'O')
    val x = isWinner(field2D, variants, 'X')
    return if (o != -1 && x != -1) {
        true
    } else !isValidCountOfSteps(arrayToString(field2D))
}

fun isValidCountOfSteps(input: String): Boolean {
    val x = countSteps(input, 'X')
    val o = countSteps(input, 'O')
    if ((x == o + 1) || (x + 1 == o)) return true
    if (x == o) return true
    return false
}

fun countSteps(input: String, symbol: Char): Int = input.count { it == symbol }

/**
 * Output:
-1 there is no winner
0-8 number of the row where we have 3 chars in row
 */
fun isWinner(field: Array<Array<String>>, variants: Array<Array<String>>, player: Char): Int {
    for (i in variants.indices) {
        val line = getRow(variants, i, field)
        if (line.count { it == player } == 3) {
            return i
        }
    }
    return -1
}

fun getCoordFromStr(variants: Array<Array<String>>, i: Int, j: Int) =
        variants[i][j].split('.').map { it.toInt() }

private fun printField(field2D: Array<Array<String>>) {
    var input = arrayToString(field2D)

    val sb = StringBuilder("| ")
    for (i in 1..input.length) {
        if (input[i - 1] == '_') {
            sb.append("  ")
        } else {
            sb.append(input[i - 1]).append(" ")
        }
        if (i % 3 == 0) {
            sb.append("|\n| ")
        }
    }
    println("--------- ")
    println(sb.toString().removeSuffix("\n| "))
    println("--------- ")
}

private fun arrayToString(field2D: Array<Array<String>>): String {
    var input = field2D.contentDeepToString()
            .replace(",", "")
            .replace("[", "")
            .replace("]", "")
            .replace(" ", "")
    return input
}

fun initVariants(): Array<Array<String>> {
    var variants = emptyArray<Array<String>>()
    for (i in 0..2) {
        variants += arrayOf("$i.0", "$i.1", "$i.2")
    }
    for (i in 0..2) {
        variants += arrayOf("0.$i", "1.$i", "2.$i")
    }
    variants += arrayOf("0.0", "1.1", "2.2")
    variants += arrayOf("2.0", "1.1", "0.2")
    return variants
}