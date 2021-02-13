package ru.netology.funnytictactoe.logic

import android.view.DragEvent
import android.view.View
import ru.netology.funnytictactoe.databinding.ActivityMainBinding
import ru.netology.funnytictactoe.view.DrugAndDropHandler
import ru.netology.funnytictactoe.view.GameFieldHelper

interface GameConfiguration {
    fun displayAnimation(sing: String, anim1: () -> Unit, anim2: () -> Unit)
    fun spawnPlayersSigns(dndHelper: DrugAndDropHandler)
    fun doGamePlay()
    fun checkDropCheating(event: DragEvent?, v: View?)
    fun initUI( lcl : View.OnLongClickListener,  gfh : GameFieldHelper)
}