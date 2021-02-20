package ru.netology.funnytictactoe.logic

import android.view.DragEvent
import android.view.View
import ru.netology.funnytictactoe.view.DrugAndDropHandler
import ru.netology.funnytictactoe.view.GameFieldHelper
import ru.netology.funnytictactoe.R
import ru.netology.funnytictactoe.view.computerUI

class ComputerPlayer(private var gfh : GameFieldHelper) : GameConfiguration {
    override fun displayAnimation(sing: String, anim1: () -> Unit, anim2: () -> Unit) {
        anim2.invoke()
    }

    override fun spawnPlayersSigns(dndHelper: DrugAndDropHandler) {
        gfh.spawn(gfh.binding.cellXSpawn, R.drawable.x_sign, Consts.x,dndHelper)
    }

    override fun doGamePlay() {
        with (gfh) {
            updateDataFromFieldtoArray(gameState)

            var message = calcGameResults(gameState)


            displayInfo(message)

            if (message == Consts.draw) {
                clearGameField()
                displayInfo(message)
                computersMove()
                updateDataFromFieldtoArray(gameState)
                return
            }
            if (getWinnersRow(gameState).winner == Consts.human) {
                playRowDisappearAnimationAndClearField(getWinnersRow(gameState)) {}
                return
            }

            computersMove()
            updateDataFromFieldtoArray(gameState)

            message = calcGameResults(gameState)
            if (message == Consts.draw) {
                clearGameField()
                displayInfo(message)
                computersMove()
                updateDataFromFieldtoArray(gameState)
                return
            }

            if (getWinnersRow(gameState).winner == Consts.computer) {
                playRowDisappearAnimationAndClearField(
                    getWinnersRow(gameState)
                ) {
                    computersMove()
                    updateDataFromFieldtoArray(gameState)
                }
                displayInfo(message)
            }
        }
    }


    override  fun checkDropCheating(event: DragEvent?, v: View?) {
        gfh.acceptDrop(event, v)
    }

    override fun initUI( lcl : View.OnLongClickListener,  gfh : GameFieldHelper) {
        computerUI(lcl, gfh)
    }
}