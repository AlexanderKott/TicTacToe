package ru.netology.funnytictactoe

import android.content.Context
import android.content.res.Resources
import android.text.Html
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import ru.netology.funnytictactoe.databinding.ActivityMainBinding

class GameFieldHelper(
    val context: Context,
    val binding: ActivityMainBinding,
    val game: Game,
    val resources: Resources
) {


    private val fields = arrayOf(
        binding.cell0, binding.cell1, binding.cell2, binding.cell3,
        binding.cell4, binding.cell5, binding.cell6, binding.cell7,
        binding.cell8
    )

    fun performGamePlayChecks() {
        updateDataFromFieldtoArray(game)

        binding.info.text = game.gameField.contentDeepToString()
        var message = calcGameResults(game)
        if (game.cheating) {
            message = " hey! what are you doing?"
            game.cheating = false
        }


        binding.gameInfo
            .text = Html.fromHtml(
            "<font color=\"blue\">X score:${game.xScores}</font>" +
                    "&nbsp;&nbsp;&nbsp;<b>$message</b>&nbsp;&nbsp;&nbsp;<font color=\"red\">O score:" +
                    "${game.oScores}</font>"
        )


        val gameResult = getWinner(game)

        if (gameResult.weHaveWinner) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.scale_in);

            for (f in fields) {
                for (item in gameResult.WinnersRow)
                    if ((f.tag as OTag).number == item) {
                        if (f.getChildAt(0) != null) {
                            f.getChildAt(0).background =
                                resources.getDrawable(R.drawable.shadow_red)
                            f.getChildAt(0).startAnimation(animation)
                        }
                    }
            }

            animation.setAnimationListener(object : Animation.AnimationListener {
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

    /**
     * move cells data from UI to array
     */
    private fun updateDataFromFieldtoArray(game: Game) {
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

    fun clearGameField() {
        for (f in fields) {
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
        if (container.getChildAt(0) != null) {
            game.cheating = true
        }
        container.removeAllViews()
        container.addView(vw)
        vw.visibility = View.VISIBLE
    }
}