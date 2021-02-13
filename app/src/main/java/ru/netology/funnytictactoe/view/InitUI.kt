package ru.netology.funnytictactoe.view

import android.graphics.Point
import android.view.View
import ru.netology.funnytictactoe.R


fun computerUI(lcl: View.OnLongClickListener, gfh: GameFieldHelper) {
    gfh.spawn(gfh.binding.cellXSpawn, R.drawable.x_sign, "X", lcl)
    gfh.binding.moveOinfo.text = "play angainst computer"
    gfh.spawnComputerIcon()
}


fun humanUI(lcl: View.OnLongClickListener, gfh: GameFieldHelper) {
    gfh.spawn(gfh.binding.cellOSpawn, R.drawable.x_sign, "O", lcl)
    gfh.spawn(gfh.binding.cellXSpawn, R.drawable.o_sign, "X", lcl)
}


class OTag(val number: Int, val point: Point)