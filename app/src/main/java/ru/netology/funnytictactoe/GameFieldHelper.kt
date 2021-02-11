package ru.netology.funnytictactoe

import android.content.Context
import android.graphics.Point
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import ru.netology.funnytictactoe.databinding.ActivityMainBinding

class GameFieldHelper(val context: Context,
                      val binding : ActivityMainBinding,
                      val game :Game)  {

    private val fields = arrayOf(binding.cell0,binding.cell1,binding.cell2,binding.cell3,
            binding.cell4,binding.cell5,binding.cell6,binding.cell7,
            binding.cell8)

    fun gameLoop(game : Game) {
        updateField(game)

       // binding.info.text = game.gameField.contentDeepToString()
        val message = calcGameResults(game)

        binding.gameInfo.text = "X score:${game.xScores}    $message    O score:${game.oScores}"
        val gameResult = getWinner(game)

        if (gameResult.weHaveWinner) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.scale_in);
           // binding.moveOinfo.startAnimation(animation)

            for (f in fields){
                 for (item in gameResult.WinnersRow)
                    if  ((f.tag as OTag).number == item){
                        if (f.getChildAt(0) != null){
                            f.getChildAt(0).startAnimation(animation)
                        }
                    }

            }

            animation.setAnimationListener( object:  Animation.AnimationListener  {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    clearGameField()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })

       }
    }

    private fun updateField(game: Game) {
        for (f in fields) {
            if (f.getChildAt(0) != null) {
                game.gameField[(f.tag as OTag).point.x][(f.tag as OTag).point.y] =
                        (f.getChildAt(0) as ImageView).tag.toString()
                (f.getChildAt(0) as ImageView).setOnLongClickListener(null)
            } else {
                game.gameField[(f.tag as OTag).point.x][(f.tag as OTag).point.y] = "_"
            }
        }
    }

    fun clearGameField(){
        for (f in fields){
            f.removeAllViews()
        }
    }

    /**Add X or O to a cell of field
     *
     */
    fun acceptDrop(event: DragEvent?, v: View?) {
        val vw: View = event?.localState as View;
        val owner: ViewGroup = vw.parent as ViewGroup
        owner.removeView(vw); //remove the dragged view
        val container: LinearLayout = v as LinearLayout;
        container.removeAllViews()
        container.addView(vw);
        vw.setVisibility(View.VISIBLE);
    }
}