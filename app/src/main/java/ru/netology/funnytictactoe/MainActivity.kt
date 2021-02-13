package ru.netology.funnytictactoe

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import ru.netology.funnytictactoe.databinding.ActivityMainBinding
import ru.netology.funnytictactoe.logic.ComputerPlayer
import ru.netology.funnytictactoe.logic.GameState
import ru.netology.funnytictactoe.logic.initGameField
import ru.netology.funnytictactoe.view.*


class MainActivity : AppCompatActivity(){
    private lateinit var binding :  ActivityMainBinding
    private lateinit var dndHelper: DrugAndDropHandler
    private lateinit var gameState : GameState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configurateScreen()

        gameState = initGameField()
        val gfh = GameFieldHelper( binding, gameState, resources)
        val gameConfiguration  = ComputerPlayer(gfh)
        dndHelper = DrugAndDropHandler(binding, gameConfiguration)

        gameConfiguration.initUI (dndHelper, gfh )


        binding.moveXInfo.animation = (AnimationUtils.loadAnimation(this, R.anim.blink))
        gfh.initFieldLayout(binding, dndHelper)
    }

    private fun configurateScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }





    private fun fullScreenCall() {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
    }

    override fun onResume() {
        super.onResume()
        fullScreenCall()
    }



}

