package ru.netology.funnytictactoe

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import ru.netology.funnytictactoe.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){
    lateinit var binding :  ActivityMainBinding
    lateinit var dndHelper: View.OnDragListener
    private val game = initGame()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val gfh = GameFieldHelper(this, binding, game)
        dndHelper = DrugAndDropHandler(this, binding, game, gfh, resources)



        initSpawners()

        initFieldLayout(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSpawners() {
        val x = binding.XPos
        val o = binding.OPos
        x.setOnLongClickListener(dndHelper as View.OnLongClickListener)
        o.setOnLongClickListener(dndHelper as View.OnLongClickListener)
        x.tag = "X"
        o.tag = "O"

        binding.moveXInfo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))

     /*      binding.cellOSpawn.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (event?.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.moveOinfo.setText(getString(R.string.infoText))
                    return true;
                }

                if (event?.getAction() == MotionEvent.ACTION_UP) {
                    binding.moveOinfo.setText(getString(R.string.infoText2))
                    return true;
                }

                return false;
            }
        })*/

    }



    private fun initFieldLayout(binding: ActivityMainBinding) {
       binding.cell0.setOnDragListener(dndHelper)
       binding.cell0.tag = OTag(0 , Point(0, 0))

        binding.cell1.setOnDragListener(dndHelper)
        binding.cell1.tag = OTag(1, Point(0, 1))

       binding.cell2.setOnDragListener(dndHelper)
       binding.cell2.tag = OTag(2,Point(0, 2))

       binding.cell3.setOnDragListener(dndHelper)
       binding.cell3.tag = OTag(3,Point(1, 0))

       binding.cell4.setOnDragListener(dndHelper)
       binding.cell4.tag = OTag(4,Point(1, 1))

       binding.cell5.setOnDragListener(dndHelper)
       binding.cell5.tag = OTag(5, Point(1, 2))

       binding.cell6.setOnDragListener(dndHelper)
       binding.cell6.tag = OTag(6,Point(2, 0))

       binding.cell7.setOnDragListener(dndHelper)
       binding.cell7.tag = OTag(7, Point(2, 1))

       binding.cell8.setOnDragListener(dndHelper)
       binding.cell8.tag = OTag(8, Point(2, 2))
    }

    fun FullScreenCall() {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
    }

    override fun onResume() {
        super.onResume()
        FullScreenCall()
    }



}

data class OTag (val number: Int ,val point : Point)