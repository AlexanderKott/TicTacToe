package ru.netology.funnytictactoe.logic

import android.view.DragEvent
import android.view.View
import ru.netology.funnytictactoe.view.DrugAndDropHandler
import ru.netology.funnytictactoe.view.GameFieldHelper
import ru.netology.funnytictactoe.R
import ru.netology.funnytictactoe.view.humanUI

class HumanPlayer(private var gfh: GameFieldHelper) : GameConfiguration {
    override fun displayAnimation(sing: String, anim1: () -> Unit, anim2: () -> Unit) {
        if (sing == "X") {
            anim2.invoke()
        } else {
            anim1.invoke()
        }
    }

    override fun spawnPlayersSigns(dndHelper: DrugAndDropHandler) {
        gfh.spawn(gfh.binding.cellXSpawn, R.drawable.x_sign, "X", dndHelper)
        gfh.spawn(
            gfh.binding.cellOSpawn,
            R.drawable.o_sign,
            "O",
            dndHelper
        )
    }

    override fun doGamePlay() {
        with(gfh) {
            updateDataFromFieldtoArray(gameState)

            var message = calcGameResults(gameState)
            if (gameState.isCheating) {
                message = " hey! what are you doing?"
                gameState.isCheating = false
            }

            displayInfo(message)

            if (message == "Draw") {
                clearGameField()
                return
            }
            if (getWinnersRow(gameState).weHaveWinner) {
                playRowDisappearAnimationAndClearField(getWinnersRow(gameState)) {}
                return
            }
        }
    }


    override fun checkDropCheating(event: DragEvent?, v: View?) {
        gfh.acceptDrop(event, v)
    }

    override fun initUI(lcl: View.OnLongClickListener, gfh: GameFieldHelper) {
        humanUI(lcl, gfh)
    }
}